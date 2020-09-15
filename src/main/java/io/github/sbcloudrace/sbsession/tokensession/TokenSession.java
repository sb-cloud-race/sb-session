package io.github.sbcloudrace.sbsession.tokensession;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@RedisHash("TokenSession")
public class TokenSession {

    @Id
    private String securityToken;

    private long userId;
    private long activePersonaId;
    private String relayCryptoTicket;
    private long activeLobbyId;

    @TimeToLive
    private long timeToLive;
}
