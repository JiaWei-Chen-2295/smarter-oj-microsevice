package fun.javierchen.jcojbackendquestionservice.cache;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.benmanes.caffeine.cache.Cache;
import fun.javierchen.jcojbackendmodel.dto.questionset.QuestionSetQueryRequest;
import fun.javierchen.jcojbackendmodel.entity.QuestionSet;
import fun.javierchen.jcojbackendmodel.vo.QuestionSetVO;
import fun.javierchen.jcojbackendquestionservice.service.QuestionSetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 题目集多级缓存服务
 * <p>
 * L1(Caffeine) → L2(Redis) → DB，含穿透/击穿/雪崩防护。
 * </p>
 *
 * @author JavierChen
 */
@Service
@Slf4j
public class QuestionSetCacheService {

    private static final String REDIS_VO_PREFIX = "ojqs:vo:";
    private static final String REDIS_LIST_PREFIX = "ojqs:list:";
    private static final String REDIS_NULL_PREFIX = "ojqs:null:";
    private static final String REDIS_LOCK_PREFIX = "ojqs:lock:";

    private static final long VO_BASE_TTL_SECONDS = 30 * 60;
    private static final long VO_RANDOM_TTL_SECONDS = 5 * 60;
    private static final long LIST_BASE_TTL_SECONDS = 10 * 60;
    private static final long LIST_RANDOM_TTL_SECONDS = 2 * 60;
    private static final long NULL_TTL_SECONDS = 60;
    private static final long LOCK_TTL_SECONDS = 10;

    @Resource
    private Cache<Long, QuestionSetVO> questionSetVOCache;

    @Resource
    private Cache<String, String> questionSetListCache;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private QuestionSetService questionSetService;

    // ==================== 题目集详情缓存 ====================

    public QuestionSetVO getQuestionSetVOById(long id, HttpServletRequest request) {
        // 1. L1 Caffeine
        QuestionSetVO cached = questionSetVOCache.getIfPresent(id);
        if (cached != null) {
            log.debug("[Cache] questionSet L1 hit, id={}", id);
            return cached;
        }

        // 2. L2 Redis — 穿透防护
        String nullKey = REDIS_NULL_PREFIX + id;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(nullKey))) {
            return null;
        }

        String redisKey = REDIS_VO_PREFIX + id;
        String json = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(json)) {
            log.debug("[Cache] questionSet L2 hit, id={}", id);
            QuestionSetVO vo = JSONUtil.toBean(json, QuestionSetVO.class);
            questionSetVOCache.put(id, vo);
            return vo;
        }

        // 3. 击穿防护: 互斥锁
        String lockKey = REDIS_LOCK_PREFIX + id;
        QuestionSetVO result = null;
        try {
            boolean locked = tryLock(lockKey);
            if (!locked) {
                Thread.sleep(50);
                json = stringRedisTemplate.opsForValue().get(redisKey);
                if (StringUtils.isNotBlank(json)) {
                    result = JSONUtil.toBean(json, QuestionSetVO.class);
                    questionSetVOCache.put(id, result);
                    return result;
                }
            }

            // 查 DB
            QuestionSet questionSet = questionSetService.getById(id);
            if (questionSet == null) {
                stringRedisTemplate.opsForValue().set(nullKey, "", NULL_TTL_SECONDS, TimeUnit.SECONDS);
                return null;
            }

            result = questionSetService.getQuestionSetVO(questionSet, request);

            // 回填 L1 + L2
            questionSetVOCache.put(id, result);
            long ttl = VO_BASE_TTL_SECONDS + ThreadLocalRandom.current().nextLong(VO_RANDOM_TTL_SECONDS);
            stringRedisTemplate.opsForValue().set(redisKey, JSONUtil.toJsonStr(result), ttl, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[Cache] interrupted while waiting for lock, questionSetId={}", id);
        } finally {
            unlock(lockKey);
        }

        return result;
    }

    // ==================== 题目集列表缓存 ====================

    public boolean isCacheable(QuestionSetQueryRequest request) {
        if (request == null) {
            return false;
        }
        if (StringUtils.isNotBlank(request.getTitle()) || StringUtils.isNotBlank(request.getDescription())) {
            return false;
        }
        if (request.getUserId() != null) {
            return false;
        }
        if (request.getId() != null) {
            return false;
        }
        if (request.getCurrent() > 3) {
            return false;
        }
        if (request.getPageSize() > 20) {
            return false;
        }
        return true;
    }

    private String buildListCacheKey(QuestionSetQueryRequest request) {
        String sort = StringUtils.isNotBlank(request.getSortField()) ? request.getSortField() : "default";
        return request.getCurrent() + ":" + request.getPageSize() + ":" + sort;
    }

    public Page<QuestionSetVO> listQuestionSetVOByPageWithCache(QuestionSetQueryRequest request, HttpServletRequest httpRequest) {
        if (!isCacheable(request)) {
            return queryFromDB(request, httpRequest);
        }

        String cacheKey = buildListCacheKey(request);

        // 1. L1
        String cachedJson = questionSetListCache.getIfPresent(cacheKey);
        if (cachedJson != null) {
            log.debug("[Cache] questionSet list L1 hit, key={}", cacheKey);
            return deserializePage(cachedJson);
        }

        // 2. L2
        String redisKey = REDIS_LIST_PREFIX + cacheKey;
        String json = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(json)) {
            log.debug("[Cache] questionSet list L2 hit, key={}", cacheKey);
            questionSetListCache.put(cacheKey, json);
            return deserializePage(json);
        }

        // 3. DB
        Page<QuestionSetVO> result = queryFromDB(request, httpRequest);

        String resultJson = serializePage(result);
        questionSetListCache.put(cacheKey, resultJson);
        long ttl = LIST_BASE_TTL_SECONDS + ThreadLocalRandom.current().nextLong(LIST_RANDOM_TTL_SECONDS);
        stringRedisTemplate.opsForValue().set(redisKey, resultJson, ttl, TimeUnit.SECONDS);

        return result;
    }

    private Page<QuestionSetVO> queryFromDB(QuestionSetQueryRequest request, HttpServletRequest httpRequest) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<QuestionSet> page = questionSetService.page(new Page<>(current, size),
                questionSetService.getQueryWrapper(request));
        return questionSetService.getQuestionSetVOPage(page, httpRequest);
    }

    // ==================== Page 序列化 ====================

    private String serializePage(Page<QuestionSetVO> page) {
        PageWrapper wrapper = new PageWrapper();
        wrapper.setCurrent(page.getCurrent());
        wrapper.setSize(page.getSize());
        wrapper.setTotal(page.getTotal());
        wrapper.setRecordsJson(JSONUtil.toJsonStr(page.getRecords()));
        return JSONUtil.toJsonStr(wrapper);
    }

    private Page<QuestionSetVO> deserializePage(String json) {
        PageWrapper wrapper = JSONUtil.toBean(json, PageWrapper.class);
        Page<QuestionSetVO> page = new Page<>(wrapper.getCurrent(), wrapper.getSize(), wrapper.getTotal());
        page.setRecords(JSONUtil.toList(wrapper.getRecordsJson(), QuestionSetVO.class));
        return page;
    }

    @lombok.Data
    private static class PageWrapper {
        private long current;
        private long size;
        private long total;
        private String recordsJson;
    }

    // ==================== Redis 锁 ====================

    private boolean tryLock(String lockKey) {
        Boolean result = stringRedisTemplate.opsForValue()
                .setIfAbsent(lockKey, "1", LOCK_TTL_SECONDS, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(result);
    }

    private void unlock(String lockKey) {
        stringRedisTemplate.delete(lockKey);
    }
}
