package app.codeodyssey.codeodysseyapi.enrollment.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.enrollment.service.EnrollmentResponse;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import app.codeodyssey.codeodysseyapi.invitation.util.InvitationFactory;
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
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Create Enrollment End to End tests")
@Testcontainers
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CreateEnrollmentEndToEndTest {
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
        url = "http://localhost:%d/api/v1/invitations".formatted(port);
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
    @DisplayName("createEnrollmentEndpoint returns enrollment given an invitation id")
    void createEnrollmentEndpoint_givenInvitationId_returnsEnrollment() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var student = UserFactory.sampleUserStudent();
        var invitation = InvitationFactory.sampleInvitation(LocalDate.now().plusMonths(1), course);
        var authTokenPair = AuthenticationTokenPairFactory.sampleAuthenticationTokenPair(student);
        userRepository.saveAll(List.of(professor, student));
        courseRepository.save(course);
        invitationRepository.save(invitation);
        refreshTokenRepository.save(authTokenPair.getRefreshToken());
        httpHeaders.setBearerAuth(authTokenPair.getAccessToken());
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);
        url += "/%s/enrollments".formatted(student.getId());

        var response = assertDoesNotThrow(
                () -> restTemplate.exchange(url, HttpMethod.POST, request, EnrollmentResponse.class));

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(EnrollmentResponse.class);
    }
}
