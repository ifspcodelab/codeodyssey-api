package app.codeodyssey.codeodysseyapi.invitation.service;

import app.codeodyssey.codeodysseyapi.common.exception.*;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.invitation.api.InvitationResponse;
import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

        if (user.get().getRole().equals(UserRole.STUDENT)) {
            throw new ForbiddenAccessException(user.get().getId());
        }

        Optional<Course> course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new ResourceNotFoundException(courseId, Resource.COURSE);
        }

        if (!course.get().getProfessor().getId().equals(user.get().getId())) {
            throw new ForbiddenAccessException(user.get().getId());
        }

        if (command.expirationDate().isBefore(LocalDate.now())) {
            throw new ViolationException(
                    Resource.INVITATION,
                    ViolationType.INVITATION_EXPIRATION_DATE_BEFORE_TODAY,
                    command.expirationDate().toString());
        }

        if (command.expirationDate().isAfter(course.get().getEndDate())) {
            throw new ViolationException(
                    Resource.INVITATION,
                    ViolationType.INVITATION_EXPIRATION_DATE_AFTER_COURSE_END_DATE,
                    command.expirationDate().toString());
        }

        Invitation invitation = new Invitation(command.expirationDate(), course.get());
        invitationRepository.save(invitation);

        return invitationMapper.to(invitation);
    }
}
