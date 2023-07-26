package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.service.CreateCourseCommand;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.service.CreateUserCommand;
import app.codeodyssey.codeodysseyapi.user.service.CreateUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Tests for Create Course Endpoint")
public class CreateCourseEndpointTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    CreateCourseCommand courseCommand;
    CreateUserCommand userCommand;
    UserResponse userResponse;

    @BeforeEach
    public void setUp() {
        courseRepository.deleteAll();
        userRepository.deleteAll();

        courseCommand = new CreateCourseCommand("CourseName", "Slug",  LocalDate.now(), LocalDate.now());
        userCommand = new CreateUserCommand("UserName", "Email",  "Password");
        userResponse = createUserService.execute(userCommand);
    }

    @AfterEach
    public void tearDown() {
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("register a new course when given a valid course request")
    void createCourse_givenValidCourseRequest_return201Created() throws Exception{
        String professorId = String.valueOf(userResponse.id());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/users/" + professorId + "/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(courseCommand)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(courseCommand.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.slug").value(courseCommand.slug()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(courseCommand.startDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(courseCommand.endDate().toString()));

        Assertions.assertThat(courseCommand).isNotNull();
    }

    @Test
    @DisplayName("returns conflict when given a existing course")
    void createCourse_givenExistingCourse_return409Conflict() throws Exception{
        User user = userRepository.findById(userResponse.id()).orElseThrow(() -> new ResourceNotFoundException(userResponse.id(), Resource.USER));
        Course existingCourse = new Course("CourseName", "Slug",  LocalDate.now(), LocalDate.now(), user);
        courseRepository.save(existingCourse);
        String professorId = String.valueOf(userResponse.id());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/users/" + professorId + "/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseCommand)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Course already exists"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value(courseCommand.slug()));

        Assertions.assertThat(courseCommand).isNotNull();
    }

    @Test
    @DisplayName("returns conflict when the start date is before the current date")
    void createCourse_givenStartDateBeforeCurrentDate_return409Conflict() throws Exception{
        CreateCourseCommand courseCommand2 = new CreateCourseCommand("CourseName", "Slug",  LocalDate.of(1000, 01, 01), LocalDate.now());
        String professorId = String.valueOf(userResponse.id());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/users/" + professorId + "/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseCommand2)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Course start date is in the past"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value(courseCommand2.startDate().toString()));

        Assertions.assertThat(courseCommand2).isNotNull();
    }

    @Test
    @DisplayName("returns conflict when the end date is before the start date")
    void createCourse_givenEndDateBeforeStartDate_return409Conflict() throws Exception{
        CreateCourseCommand courseCommand2 = new CreateCourseCommand("CourseName", "Slug",  LocalDate.now(), LocalDate.of(1000, 01, 01));
        String professorId = String.valueOf(userResponse.id());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/users/" + professorId + "/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseCommand2)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Course end date is earlier than its start date"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value(courseCommand2.endDate().toString()));

        Assertions.assertThat(courseCommand2).isNotNull();
    }
}