package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.token.api.TokenResponse;
import app.codeodyssey.codeodysseyapi.token.service.GetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginUserEndpoint {

    private final GetTokenService getTokenService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserRequest userRequest){
        return new ResponseEntity<>(getTokenService.execute(userRequest), HttpStatus.OK);
    }
}
