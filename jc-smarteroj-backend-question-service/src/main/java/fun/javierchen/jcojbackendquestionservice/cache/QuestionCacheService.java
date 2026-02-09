package fun.javierchen.jcojbackendquestionservice.cache;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.benmanes.caffeine.cache.Cache;
import fun.javierchen.jcojbackendmodel.dto.question.QuestionQueryRequest;
import fun.javierchen.jcojbackendmodel.entity.Question;
import fun.javierchen.jcojbackendmodel.vo.QuestionVO;
import fun.javierchen.jcojbackendquestionservice.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 题目多级缓存服务
 * <p>
 * L1(Caffeine) → L2(Redis) → DB，含穿透/击穿/雪崩防护。
 * </p>
 *
 * @author JavierChen
 */
@Service
@Slf4j
public class QuestionCacheService {

    private static final String REDIS_VO_PREFIX = "ojq:vo:";
    private static final String REDIS_LIST_PREFIX = "ojq:list:";
    private static final String REDIS_NULL_PREFIX = "ojq:null:";
    private static final String REDIS_LOCK_PREFIX = "ojq:lock:";

    /** 详情基础TTL: 30min */
    private static final long VO_BASE_TTL_SECONDS = 30 * 60;
    /** 详情TTL随机偏移上限: 5min */
    private static final long VO_RANDOM_TTL_SECONDS = 5 * 60;
    /** 列表基础TTL: 10min */
    private static final long LIST_BASE_TTL_SECONDS = 10 * 60;
    /** 列表TTL随机偏移上限: 2min */
    private static final long LIST_RANDOM_TTL_SECONDS = 2 * 60;
    /** 空值TTL: 1min */
    private static final long NULL_TTL_SECONDS = 60;
    /** 互斥锁TTL: 10s */
    private static final long LOCK_TTL_SECONDS = 10;

    @Resource
    private Cache<Long, QuestionVO> questionVOCache;

    @Resource
    private Cache<String, String> questionListCache;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private QuestionService questionService;

    // ==================== 题目详情缓存 ====================

    /**
     * 根据 ID 获取题目 VO（多级缓存）
     *
     * @return QuestionVO or null
     */
    public QuestionVO getQuestionVOById(long id) {
        // 1. L1 Caffeine
        QuestionVO cached = questionVOCache.getIfPresent(id);
        if (cached != null) {
            log.debug("[Cache] L1 hit, questionId={}", id);
            return cached;
        }

        // 2. L2 Redis
        String redisKey = REDIS_VO_PREFIX + id;
        String nullKey = REDIS_NULL_PREFIX + id;

        // 穿透防护: 检查空值标记
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(nullKey))) {
            log.debug("[Cache] null marker hit, questionId={}", id);
            return null;
        }

        String json = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(json)) {
            log.debug("[Cache] L2 hit, questionId={}", id);
            QuestionVO vo = JSONUtil.toBean(json, QuestionVO.class);
            questionVOCache.put(id, vo); // 回填 L1
            return vo;
        }

        // 3. 击穿防护: 互斥锁重建
        String lockKey = REDIS_LOCK_PREFIX + id;
        QuestionVO result = null;
        try {
            boolean locked = tryLock(lockKey);
            if (!locked) {
                // 获取锁失败，等待后重试读 L2
                Thread.sleep(50);
                json = stringRedisTemplate.opsForValue().get(redisKey);
                if (StringUtils.isNotBlank(json)) {
                    result = JSONUtil.toBean(json, QuestionVO.class);
                    questionVOCache.put(id, result);
                    return result;
                }
                // 仍然没有，直接查DB（不再等待）
            }

            // 查 DB
            Question question = questionService.getById(id);
            if (question == null) {
                // 穿透防护: 写入空值标记
                stringRedisTemplate.opsForValue().set(nullKey, "", NULL_TTL_SECONDS, TimeUnit.SECONDS);
                return null;
            }

            result = questionService.getQuestionVO(question);

            // 回填 L1 + L2（TTL随机偏移防雪崩）
            questionVOCache.put(id, result);
            long ttl = VO_BASE_TTL_SECONDS + ThreadLocalRandom.current().nextLong(VO_RANDOM_TTL_SECONDS);
            stringRedisTemplate.opsForValue().set(redisKey, JSONUtil.toJsonStr(result), ttl, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[Cache] interrupted while waiting for lock, questionId={}", id);
        } finally {
            unlock(lockKey);
        }

        return result;
    }

    // ==================== 题目列表缓存 ====================

    /**
     * 判断列表请求是否可缓存
     * <p>
     * 条件: 无筛选条件、无搜索、页码<=3、pageSize<=20
     * </p>
     */
    public boolean isCacheable(QuestionQueryRequest request) {
        if (request == null) {
            return false;
        }
        // 有标签筛选
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            return false;
        }
        // 有关键词搜索
        if (StringUtils.isNotBlank(request.getTitle()) || StringUtils.isNotBlank(request.getContent())) {
            return false;
        }
        // 有用户ID筛选
        if (request.getUserId() != null) {
            return false;
        }
        // 有ID精确查询
        if (request.getId() != null) {
            return false;
        }
        // 页码 > 3
        if (request.getCurrent() > 3) {
            return false;
        }
        // pageSize > 20
        if (request.getPageSize() > 20) {
            return false;
        }
        return true;
    }

    /**
     * 构建列表缓存Key
     */
    private String buildListCacheKey(QuestionQueryRequest request) {
        String sort = StringUtils.isNotBlank(request.getSortField()) ? request.getSortField() : "default";
        return request.getCurrent() + ":" + request.getPageSize() + ":" + sort;
    }

    /**
     * 分页获取题目列表 VO（多级缓存）
     *
     * @return 如果可缓存且命中则返回缓存结果，否则返回 null（调用方需自行查DB）
     */
    public Page<QuestionVO> listQuestionVOByPageWithCache(QuestionQueryRequest request, HttpServletRequest httpRequest) {
        if (!isCacheable(request)) {
            // 不可缓存，直接查DB
            return queryFromDB(request, httpRequest);
        }

        String cacheKey = buildListCacheKey(request);

        // 1. L1 Caffeine
        String cachedJson = questionListCache.getIfPresent(cacheKey);
        if (cachedJson != null) {
            log.debug("[Cache] list L1 hit, key={}", cacheKey);
            return deserializePage(cachedJson);
        }

        // 2. L2 Redis
        String redisKey = REDIS_LIST_PREFIX + cacheKey;
        String json = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(json)) {
            log.debug("[Cache] list L2 hit, key={}", cacheKey);
            questionListCache.put(cacheKey, json); // 回填 L1
            return deserializePage(json);
        }

        // 3. 查 DB
        Page<QuestionVO> result = queryFromDB(request, httpRequest);

        // 回填 L1 + L2
        String resultJson = serializePage(result);
        questionListCache.put(cacheKey, resultJson);
        long ttl = LIST_BASE_TTL_SECONDS + ThreadLocalRandom.current().nextLong(LIST_RANDOM_TTL_SECONDS);
        stringRedisTemplate.opsForValue().set(redisKey, resultJson, ttl, TimeUnit.SECONDS);

        return result;
    }

    private Page<QuestionVO> queryFromDB(QuestionQueryRequest request, HttpServletRequest httpRequest) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(request));
        return questionService.getQuestionVOPage(questionPage, httpRequest);
    }

    // ==================== Page 序列化/反序列化 ====================

    /**
     * 序列化 Page 对象为 JSON（保留分页元数据）
     */
    private String serializePage(Page<QuestionVO> page) {
        PageWrapper wrapper = new PageWrapper();
        wrapper.setCurrent(page.getCurrent());
        wrapper.setSize(page.getSize());
        wrapper.setTotal(page.getTotal());
        wrapper.setRecordsJson(JSONUtil.toJsonStr(page.getRecords()));
        return JSONUtil.toJsonStr(wrapper);
    }

    /**
     * 反序列化 JSON 为 Page 对象
     */
    private Page<QuestionVO> deserializePage(String json) {
        PageWrapper wrapper = JSONUtil.toBean(json, PageWrapper.class);
        Page<QuestionVO> page = new Page<>(wrapper.getCurrent(), wrapper.getSize(), wrapper.getTotal());
        page.setRecords(JSONUtil.toList(wrapper.getRecordsJson(), QuestionVO.class));
        return page;
    }

    /**
     * Page 序列化包装器（保留分页元数据）
     */
    @lombok.Data
    private static class PageWrapper {
        private long current;
        private long size;
        private long total;
        private String recordsJson;
    }

    // ==================== Redis 分布式锁 ====================

    private boolean tryLock(String lockKey) {
        Boolean result = stringRedisTemplate.opsForValue()
                .setIfAbsent(lockKey, "1", LOCK_TTL_SECONDS, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(result);
    }

    private void unlock(String lockKey) {
        stringRedisTemplate.delete(lockKey);
    }
}
