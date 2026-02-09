package fun.javierchen.jcojbackendquestionservice.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import fun.javierchen.jcojbackendmodel.vo.QuestionSetVO;
import fun.javierchen.jcojbackendmodel.vo.QuestionVO;
import fun.javierchen.jcojbackendmodel.vo.UserVO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 多级缓存配置 — L1 Caffeine 本地缓存
 *
 * @author JavierChen
 */
@Configuration
public class CacheConfig {

    /**
     * 题目详情缓存 (L1)
     * key: questionId, value: QuestionVO
     */
    @Bean
    public Cache<Long, QuestionVO> questionVOCache() {
        return Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    /**
     * 题目列表缓存 (L1)
     * key: "page:size:sort", value: 序列化的 Page JSON
     */
    @Bean
    public Cache<String, String> questionListCache() {
        return Caffeine.newBuilder()
                .maximumSize(50)
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    /**
     * 题目集详情缓存 (L1)
     * key: questionSetId, value: QuestionSetVO
     */
    @Bean
    public Cache<Long, QuestionSetVO> questionSetVOCache() {
        return Caffeine.newBuilder()
                .maximumSize(200)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    /**
     * 题目集列表缓存 (L1)
     * key: "page:size", value: 序列化的 Page JSON
     */
    @Bean
    public Cache<String, String> questionSetListCache() {
        return Caffeine.newBuilder()
                .maximumSize(30)
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    /**
     * 用户 VO 客户端缓存 (L1)
     * key: userId, value: UserVO
     * 用于消除 Feign N+1 调用瓶颈
     */
    @Bean
    public Cache<Long, UserVO> userVOCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }
}
