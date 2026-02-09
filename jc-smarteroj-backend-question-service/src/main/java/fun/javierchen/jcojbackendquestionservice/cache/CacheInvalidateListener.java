package fun.javierchen.jcojbackendquestionservice.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.Resource;

/**
 * Redis Pub/Sub 缓存失效监听器
 * <p>
 * 监听 "ojq:cache:invalidate" 频道，收到消息后清理本节点 L1 缓存。
 * 用于多节点部署时的 L1 缓存同步。
 * </p>
 *
 * @author JavierChen
 */
@Configuration
@Slf4j
public class CacheInvalidateListener {

    @Resource
    private CacheInvalidator cacheInvalidator;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListener listener = (message, pattern) -> {
            String body = new String(message.getBody());
            log.debug("[CachePubSub] received invalidate message: {}", body);
            cacheInvalidator.invalidateLocalOnly(body);
        };

        container.addMessageListener(listener, new ChannelTopic(CacheInvalidator.INVALIDATE_CHANNEL));
        return container;
    }
}
