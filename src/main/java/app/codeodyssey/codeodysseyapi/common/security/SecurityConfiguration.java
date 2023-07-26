package app.codeodyssey.codeodysseyapi.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
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
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;

    private static final String[] NO_AUTH_REQUIRED = {
        "/api/v1/login", "/swagger-ui/**", "/v3/api-docs/**", "/api/v1/refreshtoken",
    };

    private static final String[] STUDENTS_ALLOWED = {};

    private static final String[] PROF_ALLOWED = {};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.requestMatchers(NO_AUTH_REQUIRED)
                            .permitAll()
                            .requestMatchers(STUDENTS_ALLOWED)
                            .hasAnyAuthority("STUDENT")
                            .requestMatchers(PROF_ALLOWED)
                            .hasAnyAuthority("PROFESSOR")
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
            return http.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
