package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.service.EmailRequest;
import app.codeodyssey.codeodysseyapi.user.service.SendEmailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/notify")
public class EmailEndpoint {
    private final SendEmailService sendEmailService;

    @PostMapping("/user")
    public void createEmailNotification(@Valid @RequestBody EmailRequest request) {
        sendEmailService.sendEmail(request.email(), request.token());
    }
}

