package app.codeodyssey.codeodysseyapi.common.security;

import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenException;
import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenType;
import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.token.data.RefreshToken;
import app.codeodyssey.codeodysseyapi.token.data.RefreshTokenRepository;
import app.codeodyssey.codeodysseyapi.token.data.RefreshTokenStatus;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;
    private final RefreshTokenRepository tokenRepository;
    private final UserRepository userRepository;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmail(String token) {
        return String.valueOf(extractAllClaims(token).get("email"));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(this.userRepository
                        .findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new BadCredentialsException("Bad credentials"))
                        .getId()
                        .toString())
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(DateUtils.addMinutes(new Date(), jwtConfig.getAccessTokenExp()))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isAccessTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parse(token);
            return true;
        } catch (MalformedJwtException e) {
            log.warn("invalid jwt: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("jwt is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("jwt is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("jwt claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public Optional<RefreshToken> findRefreshTokenById(UUID id) {
        return this.tokenRepository.findById(id);
    }

    public RefreshToken generateRefreshToken(UUID id, String usedToken) {
        RefreshToken refreshToken = new RefreshToken();

        if (usedToken != null) {
            Optional<RefreshToken> usedRefreshToken = this.tokenRepository.findById(UUID.fromString(usedToken));
            this.tokenRepository.setUsedById(usedRefreshToken.get().getId());
        }

        refreshToken.setUser(this.userRepository.findById(id).get());
        refreshToken.setExpiryAt(Instant.now().plus(jwtConfig.getRefreshTokenExp(), ChronoUnit.MINUTES));

        refreshToken = this.tokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyRefreshTokenExpiration(RefreshToken token) {
        if (token.getExpiryAt().compareTo(Instant.now()) < 0) {
            this.tokenRepository.setUsedById(token.getId());
            throw new ForbiddenException(
                    Resource.REFRESH_TOKEN, ForbiddenType.EXPIRED_REFRESH_TOKEN, "refresh token was expired");
        }

        return token;
    }

    public RefreshToken verifyRefreshTokenUsed(RefreshToken token) {
        if (token.getStatus().equals(RefreshTokenStatus.USED)) {
            throw new ForbiddenException(
                    Resource.REFRESH_TOKEN, ForbiddenType.REFRESH_TOKEN_USED, "refresh token already used");
        }

        return token;
    }
}
