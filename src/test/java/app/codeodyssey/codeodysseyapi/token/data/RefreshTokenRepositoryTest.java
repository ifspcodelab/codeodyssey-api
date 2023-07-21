package app.codeodyssey.codeodysseyapi.token.data;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.token.util.RefreshTokenFactory;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@DisplayName("tests for the refresh token repository")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = { DatabaseContainerInitializer.class })
public class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save a valid token when successful")
    void save_givenAValidToken_returnsToken(){
        User user = this.userRepository.save(UserFactory.createValidUser());
        RefreshToken refreshToken = RefreshTokenFactory.createValidRefreshToken(user);
        RefreshToken saved = this.refreshTokenRepository.save(refreshToken);
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getStatus()).isEqualTo(RefreshTokenStatus.UNUSED);
    }

    @Test
    @DisplayName("save a valid and updated token when successful")
    void save_givenAValidAndUpdatedToken_returnsToken(){
        User user = this.userRepository.save(UserFactory.createValidUser());
        RefreshToken refreshToken = RefreshTokenFactory.createValidRefreshToken(user);
        RefreshToken saved = this.refreshTokenRepository.save(refreshToken);
        saved.setStatus(RefreshTokenStatus.USED);
        RefreshToken updated = this.refreshTokenRepository.save(saved);
        Assertions.assertThat(updated).isNotNull();
        Assertions.assertThat(updated.getId()).isNotNull();
        Assertions.assertThat(saved.getStatus()).isEqualTo(RefreshTokenStatus.USED);
    }

}
