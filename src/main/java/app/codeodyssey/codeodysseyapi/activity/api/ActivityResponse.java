package app.codeodyssey.codeodysseyapi.activity.api;

import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;

import java.time.Instant;
import java.util.UUID;

public record ActivityResponse (
        UUID id,
        String title,
        CourseResponse course,
        Instant startDate,
        Instant endDate){}

