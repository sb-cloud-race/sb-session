package io.github.sbcloudrace.sbsession.lobby;

import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Component
public class LobbyListener {

    // not respecting the diamond type signature, usim SpEL
    @EventListener(condition = "#object.getValue() instanceof T(io.github.sbcloudrace.sbsession.lobby.Lobby)")
    public void onLobbyExpires(RedisKeyExpiredEvent<Lobby> object) {
        Lobby expiredLobby = (Lobby) object.getValue();
        System.out.println(expiredLobby.getEventId());
        System.out.println(expiredLobby.getLobbyId());
    }
}
