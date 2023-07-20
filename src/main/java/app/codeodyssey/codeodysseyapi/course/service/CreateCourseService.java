package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.common.exceptions.BusinessRuleException;
import app.codeodyssey.codeodysseyapi.common.exceptions.BusinessRuleType;
import app.codeodyssey.codeodysseyapi.common.exceptions.ResourceAlreadyExistsException;
import app.codeodyssey.codeodysseyapi.common.exceptions.ResourceName;
import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.user.User;
import app.codeodyssey.codeodysseyapi.user.GetUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CreateCourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final GetUserService getUserService;

    public CourseResponse execute(CreateCourseCommand command) {
        User professor = getUserService.execute(command.professorId());

        if(courseRepository.existsBySlug(command.slug())) {
            throw new ResourceAlreadyExistsException(ResourceName.COURSE, "slug", command.slug());
        }

        if(command.startDate().isBefore(LocalDate.now())) {
            throw new BusinessRuleException(BusinessRuleType.COURSE_START_DATE_BEFORE_TODAY);
        }

        if(command.endDate().isBefore(command.startDate())) {
            throw new BusinessRuleException(BusinessRuleType.COURSE_END_DATE_BEFORE_START_DATE);
        }

        Course course = courseRepository.save(new Course(
                command.name(),
                command.slug(),
                command.startDate(),
                command.endDate(),
                professor
        ));

        return courseMapper.to(course);
    }
}