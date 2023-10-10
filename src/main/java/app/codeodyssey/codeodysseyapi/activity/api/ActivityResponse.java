package app.codeodyssey.codeodysseyapi.activity.api;

import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;

import java.time.Instant;
import java.util.UUID;

public record ActivityResponse (
        UUID id,

        String title,

        String description,

        CourseResponse course,

        Instant startDate,

        Instant endDate,

        String initialFile,

        String solutionFile,

        String testFile,

        String extension){}

