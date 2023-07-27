package app.codeodyssey.codeodysseyapi.token.service;

import app.codeodyssey.codeodysseyapi.common.security.JwtService;
import app.codeodyssey.codeodysseyapi.user.api.LoginRequest;
import app.codeodyssey.codeodysseyapi.user.api.LoginResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetTokenService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse execute(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        User user = (User) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", roles.get(0));

        return new LoginResponse(
                jwtService.generateAccessToken(claims, user),
                jwtService.generateRefreshToken(user.getId(), null).getToken());
    }
}
