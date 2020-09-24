package io.github.sbcloudrace.sbsession.lobby;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LobbyRepository extends CrudRepository<Lobby, Long> {

    List<Lobby> findAllByEventId(Integer eventId);

}
