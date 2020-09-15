package io.github.sbcloudrace.sbsession.user;

import io.github.sbcloudrace.sbsession.tokensession.TokenSession;
import io.github.sbcloudrace.sbsession.tokensession.TokenSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final TokenSessionRepository tokenSessionRepository;

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public User userInfo(@PathVariable long userId) {
        return userRepository.findById(userId).orElse(new User());
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public User create(@RequestBody User user) {
        Optional<User> userById = userRepository.findById(user.getUserId());
        String securityToken = UUID.randomUUID().toString();
        user.setToken(securityToken);
        user.setTimeToLive(-1);
        if (userById.isPresent()) {
            User userTmp = userById.get();
            Optional<TokenSession> tokenSessionById = tokenSessionRepository.findById(userTmp.getToken());
            tokenSessionById.ifPresent(tokenSessionRepository::delete);
        }
        userRepository.save(user);
        TokenSession tokenSession = new TokenSession(
                user.getToken(),
                user.getUserId(),
                0L,
                "",
                0L,
                15 * 60);
        tokenSessionRepository.save(tokenSession);
        return user;
    }

}
