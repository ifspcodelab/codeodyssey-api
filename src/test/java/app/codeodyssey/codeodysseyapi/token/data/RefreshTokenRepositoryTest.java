package app.codeodyssey.codeodysseyapi.token.data;

import app.codeodyssey.codeodysseyapi.token.util.RefreshTokenUtils;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@DisplayName("tests for the refresh token repository")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save a valid token when successful")
    void saveAValidRefreshTokenWhenSuccessful(){
        User user = this.userRepository.save(UserUtils.createValidUser());
        RefreshToken refreshToken = RefreshTokenUtils.createValidRefreshToken(user);
        RefreshToken saved = this.refreshTokenRepository.save(refreshToken);
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getStatus()).isEqualTo(RefreshTokenStatus.UNUSED);
    }

}
