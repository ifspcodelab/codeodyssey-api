package app.codeodyssey.codeodysseyapi.invitation.e2e;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.invitation.api.InvitationResponse;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import app.codeodyssey.codeodysseyapi.invitation.service.InvitationCreateCommand;
import app.codeodyssey.codeodysseyapi.token.data.RefreshTokenRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.AuthenticationTokenPairFactory;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Create Invitation End to End tests")
@Testcontainers
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CreateInvitationEndToEndTest {
    @LocalServerPort
    Integer port;

    String url;

    RestTemplate restTemplate;

    HttpHeaders httpHeaders;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    InvitationRepository invitationRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void beforeEach() {
        url = "http://localhost:%d/api/v1/courses".formatted(port);
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
    }

    @AfterEach
    void afterEach() {
        refreshTokenRepository.deleteAll();
        invitationRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("createInvitationEndpoint returns invitation when an user is the given course's professor")
    void createInvitationEndpoint_givenCourseAndProfessor_returnsInvitation() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var authTokenPair = AuthenticationTokenPairFactory.sampleAuthenticationTokenPair(professor);
        userRepository.save(professor);
        courseRepository.save(course);
        refreshTokenRepository.save(authTokenPair.getRefreshToken());
        httpHeaders.setBearerAuth(authTokenPair.getAccessToken());
        HttpEntity<?> request =
                new HttpEntity<>(new InvitationCreateCommand(LocalDate.now()), httpHeaders);
        url += "/%s/invitations".formatted(course.getId());

        var response = assertDoesNotThrow(
                () -> restTemplate.exchange(url, HttpMethod.POST, request, InvitationResponse.class));

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(InvitationResponse.class);
    }

    @Test
    @DisplayName("createInvitationEndpoint returns unauthorized when a professor doesn't teach in the given course")
    void createInvitationEndpoint_givenCourseAndProfessor_returnsUnauthorized() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var professorB = UserFactory.sampleUserProfessorB();
        var authTokenPair = AuthenticationTokenPairFactory.sampleAuthenticationTokenPair(professorB);
        userRepository.saveAll(List.of(professor, professorB));
        courseRepository.save(course);
        refreshTokenRepository.save(authTokenPair.getRefreshToken());
        httpHeaders.setBearerAuth(authTokenPair.getAccessToken());
        HttpEntity<?> request =
                new HttpEntity<>(new InvitationCreateCommand(LocalDate.now()), httpHeaders);
        url += "/%s/invitations".formatted(course.getId());

        var throwable =
                catchThrowable(() -> restTemplate.exchange(url, HttpMethod.POST, request, InvitationResponse.class));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(HttpClientErrorException.class);
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(httpException.getResponseBodyAsString()).isNotNull();
    }
}
