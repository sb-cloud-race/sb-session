package io.github.sbcloudrace.sbsession;

import io.github.sbcloudrace.sbsession.lobby.Lobby;
import io.github.sbcloudrace.sbsession.lobby.LobbyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LobbyTests {

    @Autowired
    private LobbyRepository repo;

    @Test
    void contextLoads() throws InterruptedException {
        Lobby someLobby = new Lobby(333L, 666, 1000L);
        repo.save(someLobby);
        Thread.sleep(2500);
    }

}
