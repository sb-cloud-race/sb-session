package io.github.sbcloudrace.sbsession.tokensession;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@AllArgsConstructor
public class TokenSessionController {

    private final TokenSessionRepository tokenSessionRepository;

    @RequestMapping(value = "/tokensession/{securityToken}", method = RequestMethod.GET)
    @ResponseBody
    public TokenSession tokenSessionInfo(@PathVariable String securityToken) {
        return tokenSessionRepository.findById(securityToken).orElse(new TokenSession());
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
