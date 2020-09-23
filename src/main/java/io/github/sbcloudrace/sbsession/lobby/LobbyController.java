package io.github.sbcloudrace.sbsession.lobby;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping(value = "/lobby")
@AllArgsConstructor
public class LobbyController {

    private final StringRedisTemplate stringRedisTemplate;
    private final LobbyRepository lobbyRepository;

    @RequestMapping(value = "/debug", method = RequestMethod.GET)
    @ResponseBody
    public Long debug() {
        return getNextLobbyId();
    }

    @RequestMapping(value = "/{lobbyId}", method = RequestMethod.GET)
    @ResponseBody
    public Lobby getLobbyById(@PathVariable Long lobbyId) {
        Optional<Lobby> lobbyById = lobbyRepository.findById(lobbyId);
        if (lobbyById.isPresent()) {
            return lobbyById.get();
        }
        return new Lobby();
    }

    @RequestMapping(value = "/{eventId}", method = RequestMethod.POST)
    @ResponseBody
    public Lobby createLobby(@PathVariable Integer eventId) {
        Lobby lobby = new Lobby(getNextLobbyId(), eventId, 10000L);
        lobbyRepository.save(lobby);
        return lobby;
    }

    private Long getNextLobbyId() {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.increment("lobby_id");
        String lobby_id = stringStringValueOperations.get("lobby_id");
        return Long.valueOf(lobby_id);
    }
}
