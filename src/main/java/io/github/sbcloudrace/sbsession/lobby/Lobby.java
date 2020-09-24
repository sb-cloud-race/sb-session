package io.github.sbcloudrace.sbsession.lobby;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@RedisHash("Lobby")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lobby implements Serializable {

    @Id
    private Long lobbyId;

    @Indexed
    private Integer eventId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long timeToLive;

}
