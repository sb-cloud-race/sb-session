package io.github.sbcloudrace.sbsession.tokensession;


import io.github.sbcloudrace.sbsession.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/tokensession")
@AllArgsConstructor
public class TokenSessionController {

    private final TokenSessionRepository tokenSessionRepository;
    private final UserRepository userRepository;

    @RequestMapping(value = "/active-persona-id/{securityToken}", method = RequestMethod.GET)
    @ResponseBody
    public Long getActivePersonaId(@PathVariable String securityToken) {
        Optional<TokenSession> tokenSessionById = tokenSessionRepository.findById(securityToken);
        if (tokenSessionById.isPresent()) {
            TokenSession tokenSession = tokenSessionById.get();
            return tokenSession.getActivePersonaId();
        }
        return 0L;
    }

    @RequestMapping(value = "/active-persona-id/{securityToken}/{personaId}", method = RequestMethod.PUT)
    @ResponseBody
    public void setActivePersonaId(@PathVariable String securityToken, @PathVariable Long personaId) {
        Optional<TokenSession> tokenSessionById = tokenSessionRepository.findById(securityToken);
        if (tokenSessionById.isPresent()) {
            TokenSession tokenSession = tokenSessionById.get();
            tokenSession.setActivePersonaId(personaId);
            tokenSessionRepository.save(tokenSession);
        }
    }

    @RequestMapping(value = "/keepalive/{securityToken}", method = RequestMethod.PUT)
    @ResponseBody
    public void keepAlive(@PathVariable String securityToken) {
        tokenSessionRepository.findById(securityToken).ifPresent(
                tokenSession -> {
                    tokenSession.setTimeToLive(300L);
                    tokenSessionRepository.save(tokenSession);
                    userRepository.findById(tokenSession.getUserId()).ifPresent(user -> {
                        user.setTimeToLive(300L);
                        userRepository.save(user);
                    });
                }
        );
    }

}
