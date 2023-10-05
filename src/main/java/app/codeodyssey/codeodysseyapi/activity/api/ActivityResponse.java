package app.codeodyssey.codeodysseyapi.activity.api;

import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ActivityResponse (
        UUID id,
        String title,
        CourseResponse course,
        LocalDate startDate,
        LocalDate endDate,
        Instant createdAt){}

