package app.codeodyssey.codeodysseyapi.result.api;

import app.codeodyssey.codeodysseyapi.result.service.GetActivityResultsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/activities")
@RequiredArgsConstructor
public class GetActivityResults {

    private final GetActivityResultsService getActivityResultsService;

    @GetMapping("/{activityId}/results")
    public ResponseEntity<List<ResultResponse>> getAll(@PathVariable UUID activityId,
                                                       Authentication auth) {
        UserDetails principal = (UserDetails) auth.getPrincipal();
        return new ResponseEntity<>(
                getActivityResultsService.execute(activityId, principal.getUsername()), HttpStatus.OK
        );
    }
}
