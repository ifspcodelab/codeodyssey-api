package app.codeodyssey.codeodysseyapi.invitation.service;

import app.codeodyssey.codeodysseyapi.common.exception.EmailNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.UnauthorizedAccessException;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.invitation.api.InvitationResponse;
import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CreateInvitationService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;

    public InvitationResponse execute(InvitationCreateCommand command, UUID courseId, String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new EmailNotFoundException(userEmail);
        }

        if (!user.get().getRole().equals(UserRole.PROFESSOR)) {
            throw new UnauthorizedAccessException(user.get().getId());
        }

        Optional<Course> course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new ResourceNotFoundException(courseId, Resource.COURSE);
        }

        if (!course.get().getProfessor().getId().equals(user.get().getId())) {
            throw new UnauthorizedAccessException(user.get().getId());
        }

        Invitation invitation = new Invitation(command.expirationDate(), course.get());
        invitationRepository.save(invitation);
        String invitationLink = "/invites/%s".formatted(invitation.getId());

        return invitationMapper.to(invitation, invitationLink);
    }
}
