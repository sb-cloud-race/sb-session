package io.github.sbcloudrace.sbsession.tokensession;


import io.github.sbcloudrace.sbsession.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping
@AllArgsConstructor
public class TokenSessionController {

    private final TokenSessionRepository tokenSessionRepository;
    private final UserRepository userRepository;

    @RequestMapping(value = "/tokensession/{securityToken}", method = RequestMethod.GET)
    @ResponseBody
    public TokenSession tokenSessionInfo(@PathVariable String securityToken) {
        Optional<TokenSession> tokenSessionById = tokenSessionRepository.findById(securityToken);
        if (tokenSessionById.isPresent()) {
            TokenSession tokenSession = tokenSessionById.get();
            tokenSession.setTimeToLive(300L);
            tokenSessionRepository.save(tokenSession);
            return tokenSession;
        }
        return new TokenSession();
    }

    @RequestMapping(value = "/tokensession/keepalive/{securityToken}", method = RequestMethod.PUT)
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

    @RequestMapping(value = "/tokensession", method = RequestMethod.PUT)
    @ResponseBody
    public void save(@RequestBody TokenSession tokenSession) {
        if (tokenSessionRepository.findById(tokenSession.getSecurityToken()).isPresent()) {
            tokenSession.setTimeToLive(300L);
            tokenSessionRepository.save(tokenSession);
        }
    }
}
