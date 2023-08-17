package app.codeodyssey.codeodysseyapi.token.service;

import app.codeodyssey.codeodysseyapi.common.security.JwtService;
import app.codeodyssey.codeodysseyapi.user.api.LoginRequest;
import app.codeodyssey.codeodysseyapi.user.api.LoginResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class GetTokenService {

    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final UserRepository userRepository;

    public LoginResponse execute(LoginRequest request) {
        var userEmail = this.userRepository
                .findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, request.password()));

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        User user = (User) authentication.getPrincipal();

        Map<String, Object> claims = new ConcurrentHashMap<>();
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        claims.put("role", roles.get(0));

        return new LoginResponse(
                jwtService.generateAccessToken(claims, user),
                jwtService.generateRefreshToken(user.getId(), null).getId().toString());
    }
}
