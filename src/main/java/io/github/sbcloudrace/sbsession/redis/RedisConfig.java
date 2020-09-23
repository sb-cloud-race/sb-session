package io.github.sbcloudrace.sbsession.redis;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.concurrent.Executors;
import java.util.regex.Pattern;

@Configuration
@AllArgsConstructor
public class RedisConfig {

    private final KeySpaceNotificationMessageListener keySpaceNotificationMessageListener;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("127.0.0.1", 6379));
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(keySpaceNotificationMessageListener, new PatternTopic("__keyevent*__:expired"));
        container.setTaskExecutor(Executors.newFixedThreadPool(4));
        return container;
    }

}
