package fun.javierchen.jcojbackendquestionservice.cache;

import com.github.benmanes.caffeine.cache.Cache;
import fun.javierchen.jcojbackendmodel.vo.QuestionSetVO;
import fun.javierchen.jcojbackendmodel.vo.QuestionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 缓存失效处理器
 * <p>
 * 在写操作(增/删/改)后调用，清理本节点 L1 + L2 Redis + 发布 Pub/Sub 通知其他节点。
 * </p>
 *
 * @author JavierChen
 */
@Component
@Slf4j
public class CacheInvalidator {

    public static final String INVALIDATE_CHANNEL = "ojq:cache:invalidate";

    private static final String REDIS_VO_PREFIX = "ojq:vo:";
    private static final String REDIS_LIST_PREFIX = "ojq:list:";
    private static final String REDIS_NULL_PREFIX = "ojq:null:";
    private static final String REDIS_SET_VO_PREFIX = "ojqs:vo:";
    private static final String REDIS_SET_LIST_PREFIX = "ojqs:list:";

    @Resource
    private Cache<Long, QuestionVO> questionVOCache;

    @Resource
    private Cache<String, String> questionListCache;

    @Resource
    private Cache<Long, QuestionSetVO> questionSetVOCache;

    @Resource
    private Cache<String, String> questionSetListCache;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // ==================== 题目缓存失效 ====================

    /**
     * 失效单个题目详情缓存（增/删/改时调用）
     */
    public void invalidateQuestion(long questionId) {
        // L1
        questionVOCache.invalidate(questionId);
        // L2
        stringRedisTemplate.delete(REDIS_VO_PREFIX + questionId);
        stringRedisTemplate.delete(REDIS_NULL_PREFIX + questionId);
        // 通知其他节点
        publish("question:" + questionId);
        log.info("[CacheInvalidate] question:{}", questionId);
    }

    /**
     * 失效所有题目列表缓存
     */
    public void invalidateQuestionListCache() {
        // L1
        questionListCache.invalidateAll();
        // L2: 按前缀删除
        deleteByPrefix(REDIS_LIST_PREFIX);
        // 通知其他节点
        publish("questionList:all");
        log.info("[CacheInvalidate] questionList:all");
    }

    /**
     * 题目写操作后的完整失效（详情 + 列表）
     */
    public void invalidateQuestionAll(long questionId) {
        invalidateQuestion(questionId);
        invalidateQuestionListCache();
    }

    /**
     * 题目新增后的失效（仅列表）
     */
    public void onQuestionAdded() {
        invalidateQuestionListCache();
    }

    /**
     * 题目更新/删除后的失效（详情 + 列表）
     */
    public void onQuestionUpdatedOrDeleted(long questionId) {
        invalidateQuestionAll(questionId);
    }

    // ==================== 题目集缓存失效 ====================

    /**
     * 失效单个题目集详情缓存
     */
    public void invalidateQuestionSet(long questionSetId) {
        questionSetVOCache.invalidate(questionSetId);
        stringRedisTemplate.delete(REDIS_SET_VO_PREFIX + questionSetId);
        publish("questionSet:" + questionSetId);
        log.info("[CacheInvalidate] questionSet:{}", questionSetId);
    }

    /**
     * 失效所有题目集列表缓存
     */
    public void invalidateQuestionSetListCache() {
        questionSetListCache.invalidateAll();
        deleteByPrefix(REDIS_SET_LIST_PREFIX);
        publish("questionSetList:all");
        log.info("[CacheInvalidate] questionSetList:all");
    }

    /**
     * 题目集新增后的失效
     */
    public void onQuestionSetAdded() {
        invalidateQuestionSetListCache();
    }

    /**
     * 题目集更新/删除后的失效
     */
    public void onQuestionSetUpdatedOrDeleted(long questionSetId) {
        invalidateQuestionSet(questionSetId);
        invalidateQuestionSetListCache();
    }

    /**
     * 题目加入/移出集合后的失效
     */
    public void onQuestionSetItemChanged(long questionSetId) {
        invalidateQuestionSet(questionSetId);
    }

    // ==================== L1 本地失效（被 Pub/Sub 监听器调用） ====================

    /**
     * 仅清理本节点 L1 缓存（不触发 Redis 删除和 Pub/Sub）
     */
    public void invalidateLocalOnly(String message) {
        if (message == null) {
            return;
        }
        if (message.startsWith("question:") && !message.startsWith("questionList") && !message.startsWith("questionSet")) {
            String idStr = message.substring("question:".length());
            try {
                questionVOCache.invalidate(Long.parseLong(idStr));
            } catch (NumberFormatException ignored) {
            }
        } else if ("questionList:all".equals(message)) {
            questionListCache.invalidateAll();
        } else if (message.startsWith("questionSet:") && !message.startsWith("questionSetList")) {
            String idStr = message.substring("questionSet:".length());
            try {
                questionSetVOCache.invalidate(Long.parseLong(idStr));
            } catch (NumberFormatException ignored) {
            }
        } else if ("questionSetList:all".equals(message)) {
            questionSetListCache.invalidateAll();
        }
    }

    // ==================== 内部方法 ====================

    private void publish(String message) {
        try {
            stringRedisTemplate.convertAndSend(INVALIDATE_CHANNEL, message);
        } catch (Exception e) {
            log.warn("[CacheInvalidate] Pub/Sub publish failed: {}", e.getMessage());
        }
    }

    /**
     * 按前缀删除 Redis Key（使用 SCAN 避免阻塞）
     */
    private void deleteByPrefix(String prefix) {
        try {
            Set<String> keys = stringRedisTemplate.keys(prefix + "*");
            if (keys != null && !keys.isEmpty()) {
                stringRedisTemplate.delete(keys);
            }
        } catch (Exception e) {
            log.warn("[CacheInvalidate] delete by prefix failed: {}", e.getMessage());
        }
    }
}
