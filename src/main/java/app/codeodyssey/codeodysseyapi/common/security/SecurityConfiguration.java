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
    private final AuthenticationProvider authenticationProvider;

    private static final String[] NO_AUTH_REQUIRED = {
            "/api/v1/login",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/refreshtoken",
            "/api/v1/courses",
            "/api/v1/users",
            "/api/v1/users/{professorId}/courses"
    };

    private static final String[] STUDENTS_ALLOWED = {

    };

    private static final String[] PROFESSORS_ALLOWED = {
            "/api/v1/users/{professorId}/courses"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(NO_AUTH_REQUIRED).permitAll()
                        .requestMatchers(STUDENTS_ALLOWED).hasAnyAuthority("STUDENT")
                        .requestMatchers(PROFESSORS_ALLOWED).hasAnyAuthority("PROFESSOR")
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
