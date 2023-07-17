package app.codeodyssey.codeodysseyapi.common.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(JwtConfig.class)
@ConfigurationProperties(prefix = "application.jwt")
@Configuration
@NoArgsConstructor
@Getter
public class JwtConfig {

    private String secretKey;
    private Integer accessTokenExpirationAfterMinutes;
}
