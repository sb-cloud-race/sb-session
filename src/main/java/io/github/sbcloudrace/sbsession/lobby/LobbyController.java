package io.github.sbcloudrace.sbsession.lobby;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/lobby")
@AllArgsConstructor
public class LobbyController {

    private final StringRedisTemplate stringRedisTemplate;
    private final LobbyRepository lobbyRepository;

    @RequestMapping(value = "/event/{eventId}", method = RequestMethod.GET)
    @ResponseBody
    public Lobby getActiveLobbyByEventId(@PathVariable("eventId") Integer eventId) {
        List<Lobby> allLobbiesByEventId = lobbyRepository.findAllByEventId(eventId);
        allLobbiesByEventId
                // something wrong with ttl annotation, need wrapper or spring bugfix
                // https://github.com/spring-projects/spring-data-redis/pull/208/files
                .forEach(lobby -> {
                            String lobbyKey = "Lobby:".concat(lobby.getLobbyId().toString());
                            Long expire = stringRedisTemplate.getExpire(lobbyKey, TimeUnit.MILLISECONDS);
                            if (expire != null) {
                                lobby.setTimeToLive(expire);
                            }
                        }
                );
        return allLobbiesByEventId.stream().filter(lobby -> lobby.getTimeToLive() > 20000L)
                .findFirst()
                .orElse(new Lobby());
    }

    @RequestMapping(value = "/{lobbyId}", method = RequestMethod.GET)
    @ResponseBody
    public Lobby getLobbyById(@PathVariable Long lobbyId) {
        Optional<Lobby> lobbyById = lobbyRepository.findById(lobbyId);
        return lobbyById.orElseGet(Lobby::new);
    }

    @RequestMapping(value = "/event/{eventId}", method = RequestMethod.POST)
    @ResponseBody
    public Long createLobby(@PathVariable Integer eventId) {
        Long nextLobbyId = getNextLobbyId();
        Lobby lobby = new Lobby(nextLobbyId, eventId, 6000000L);
        lobbyRepository.save(lobby);
        return nextLobbyId;
    }

    private Long getNextLobbyId() {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.increment("lobby_id");
        String lobby_id = stringStringValueOperations.get("lobby_id");
        if (lobby_id == null) {
            return 1L;
        }
        return Long.valueOf(lobby_id);
    }
}
