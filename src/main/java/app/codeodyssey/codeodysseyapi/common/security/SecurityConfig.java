package app.codeodyssey.codeodysseyapi.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;

    private static final String[] NO_AUTH_REQUIRED = {
        "/api/v1/login",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/api/v1/refreshtoken",
        "api/v1/users",
        "api/v1/users/confirmation/*",
        "api/v1/users/resend-email",
        "/api/v1/users/*/courses/*/students",
        "/api/v1/users/*/courses",
        "/api/v1/users/*/courses/*/students",
        "/api/v1/invitations/*/enrollments",
        "/api/v1/invitations/*",
        "/api/v1/courses/*/activities",
        "/api/v1/courses/*/activities/*",
        "/api/v1/courses/*/activities/*/resolutions"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.requestMatchers(NO_AUTH_REQUIRED)
                            .permitAll()
                            .anyRequest()
                            .authenticated())
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authProvider)
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .formLogin(AbstractHttpConfigurer::disable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            return http.cors(Customizer.withDefaults()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
