package io.github.sbcloudrace.sbsession.user;

import io.github.sbcloudrace.sbsession.tokensession.TokenSession;
import io.github.sbcloudrace.sbsession.tokensession.TokenSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(value = "/user")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final TokenSessionRepository tokenSessionRepository;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public String createTemporarySession(@PathVariable long userId) {
        String securityToken = UUID.randomUUID().toString();
        User user = new User(userId, securityToken, 15 * 60);
        userRepository.save(user);
        return securityToken;
    }

    @RequestMapping(value = "/{userId}/{token}", method = RequestMethod.GET)
    @ResponseBody
    public String createPermanentSession(@PathVariable long userId, @PathVariable String token) {
        return createPermanentSession(new User(userId, token, -1)).getToken();
    }

    private User createPermanentSession(User user) {
        Optional<User> userById = userRepository.findById(user.getUserId());
        if (userById.isPresent() && userById.get().getToken().equals(user.getToken())) {
            String securityToken = UUID.randomUUID().toString();
            user.setToken(securityToken);
            user.setTimeToLive(300L);
            tokenSessionRepository.findById(userById.get().getToken())
                    .ifPresent(tokenSessionRepository::delete);
            userRepository.save(user);
            TokenSession tokenSession = new TokenSession(
                    user.getToken(),
                    user.getUserId(),
                    0L,
                    "",
                    0L,
                    300L);
            tokenSessionRepository.save(tokenSession);
            return user;
        }
        return new User();
    }

}
