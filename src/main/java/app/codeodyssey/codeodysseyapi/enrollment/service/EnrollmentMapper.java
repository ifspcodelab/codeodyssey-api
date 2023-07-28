package app.codeodyssey.codeodysseyapi.enrollment.service;

import app.codeodyssey.codeodysseyapi.enrollment.data.Enrollment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    EnrollmentResponse to(Enrollment enrollment);
}
