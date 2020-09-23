package io.github.sbcloudrace.sbsession.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KeySpaceNotificationMessageListener implements MessageListener {

    // to enable redis expired keys notification run inside redis-cli:
    // config set notify-keyspace-events Ex
    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("Recieved action = " + new String(message.getBody()) + " " +
                " and key info = " + new String(message.getChannel()));
    }

}
