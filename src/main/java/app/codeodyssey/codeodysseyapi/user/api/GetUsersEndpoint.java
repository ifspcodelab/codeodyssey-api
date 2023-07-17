package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.service.GetUsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class GetUsersEndpoint {
    private final GetUsersService getUsersService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> get() {
        return ResponseEntity.ok(getUsersService.execute());
    }
}
