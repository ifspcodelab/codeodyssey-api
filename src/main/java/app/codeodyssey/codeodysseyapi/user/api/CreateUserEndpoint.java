package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.service.CreateUserCommand;
import app.codeodyssey.codeodysseyapi.user.service.CreateUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
@Tag(name = "Users", description = "Users API endpoint")
public class CreateUserEndpoint {
    private final CreateUserService createUserService;

    @Operation(
            summary = "Create an user.",
            description = "Creates, saves and returns an user given a name, email and password.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                content = {
                    @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")
                }),
        @ApiResponse(
                responseCode = "409",
                content = {
                    @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                }),
        @ApiResponse(
                responseCode = "400",
                content = {
                    @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                })
    })
    @PostMapping()
    public ResponseEntity<UserResponse> post(@RequestBody @Valid CreateUserCommand command) {
        return new ResponseEntity<>(createUserService.execute(command), HttpStatus.CREATED);
    }
}
