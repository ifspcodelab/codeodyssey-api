package app.codeodyssey.codeodysseyapi.token.util;

import app.codeodyssey.codeodysseyapi.user.data.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.time.DateUtils;

public class AccessTokenFactory {
    private AccessTokenFactory() {}

    public static String sampleAccessToken(User user) {
        Map<String, Object> userClaims = new HashMap<>();
        userClaims.put("role", user.getRole().toString());

        String rawKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

        return Jwts.builder()
                .setClaims(userClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(DateUtils.addMinutes(new Date(), 15))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(rawKey)), SignatureAlgorithm.HS256)
                .compact();
    }
}
