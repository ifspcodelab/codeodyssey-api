package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.course.api.CourseDTO;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseDTO to(Course course);
    List<CourseDTO> to(List<Course> events);
}