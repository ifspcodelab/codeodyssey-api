package app.codeodyssey.codeodysseyapi.activity.api;

import app.codeodyssey.codeodysseyapi.activity.service.GetCourseActivitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GetCourseActivitiesEndpoint {

    private final GetCourseActivitiesService getCourseActivitiesService;

    @GetMapping("/courses/{courseId}/activities")
    public ResponseEntity<List<ActivityResponse>> get(@PathVariable UUID courseId, Authentication authentication) {
        return new ResponseEntity<>(this.getCourseActivitiesService.execute(courseId, authentication), HttpStatus.OK);
    }
}
