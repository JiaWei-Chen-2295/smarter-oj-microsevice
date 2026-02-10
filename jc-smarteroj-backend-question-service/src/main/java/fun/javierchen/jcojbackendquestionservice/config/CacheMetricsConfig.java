package fun.javierchen.jcojbackendquestionservice.config;

import com.github.benmanes.caffeine.cache.Cache;
import fun.javierchen.jcojbackendmodel.vo.QuestionSetVO;
import fun.javierchen.jcojbackendmodel.vo.QuestionVO;
import fun.javierchen.jcojbackendmodel.vo.UserVO;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Caffeine 缓存 Micrometer 监控绑定.
 */
@Configuration
public class CacheMetricsConfig {

    private final MeterRegistry meterRegistry;
    private final Cache<Long, QuestionVO> questionVOCache;
    private final Cache<String, String> questionListCache;
    private final Cache<Long, QuestionSetVO> questionSetVOCache;
    private final Cache<String, String> questionSetListCache;
    private final Cache<Long, UserVO> userVOCache;

    public CacheMetricsConfig(MeterRegistry meterRegistry,
                              Cache<Long, QuestionVO> questionVOCache,
                              Cache<String, String> questionListCache,
                              Cache<Long, QuestionSetVO> questionSetVOCache,
                              Cache<String, String> questionSetListCache,
                              Cache<Long, UserVO> userVOCache) {
        this.meterRegistry = meterRegistry;
        this.questionVOCache = questionVOCache;
        this.questionListCache = questionListCache;
        this.questionSetVOCache = questionSetVOCache;
        this.questionSetListCache = questionSetListCache;
        this.userVOCache = userVOCache;
    }

    @PostConstruct
    public void bindCacheMetrics() {
        CaffeineCacheMetrics.monitor(meterRegistry, questionVOCache, "questionVO");
        CaffeineCacheMetrics.monitor(meterRegistry, questionListCache, "questionList");
        CaffeineCacheMetrics.monitor(meterRegistry, questionSetVOCache, "questionSetVO");
        CaffeineCacheMetrics.monitor(meterRegistry, questionSetListCache, "questionSetList");
        CaffeineCacheMetrics.monitor(meterRegistry, userVOCache, "userVO");
    }
}
