package io.github.sbcloudrace.sbsession.redis;

import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
public class RedisExpiredKeysListenerContainer extends RedisMessageListenerContainer {

    private final RedisExpiredKeysMessageListener redisExpiredKeysMessageListener;

    private final RedisConfig redisConfig;

    public RedisExpiredKeysListenerContainer(RedisExpiredKeysMessageListener redisExpiredKeysMessageListener, RedisConfig redisConfig) {
        this.redisExpiredKeysMessageListener = redisExpiredKeysMessageListener;
        this.redisConfig = redisConfig;
        super.setConnectionFactory(this.redisConfig.redisConnectionFactory());
        super.addMessageListener(this.redisExpiredKeysMessageListener, new PatternTopic("__keyevent*__:expired"));
        super.setTaskExecutor(Executors.newFixedThreadPool(4));
    }
}
