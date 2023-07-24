package app.codeodyssey.codeodysseyapi.common.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@EnableConfigurationProperties(JwtConfig.class)
@ConfigurationProperties(prefix = "application.jwt")
@Component
@NoArgsConstructor
@Getter
@Setter
public class JwtConfig {
    private String secretKey;
    @Value("${application.jwt.access-token-expiration-after-minutes}")
    private Integer accessTokenExp;
    @Value("${application.jwt.refresh-token-expiration-after-minutes}")
    private Integer refreshTokenExp;
    private String issuer;
}
