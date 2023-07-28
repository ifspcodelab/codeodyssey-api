package app.codeodyssey.codeodysseyapi.enrollment.service;

import app.codeodyssey.codeodysseyapi.common.exception.EmailNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.StudentAlreadyEnrolledException;
import app.codeodyssey.codeodysseyapi.enrollment.data.Enrollment;
import app.codeodyssey.codeodysseyapi.enrollment.data.EnrollmentRepository;
import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateEnrollmentService {
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentResponse execute(UUID invitationId, String userEmail) {
        Optional<User> studentOpt = userRepository.findByEmail(userEmail);

        if (studentOpt.isEmpty()) {
            throw new EmailNotFoundException(userEmail);
        }

        User student = studentOpt.get();

        Optional<Invitation> invitationOpt = invitationRepository.findById(invitationId);

        if (invitationOpt.isEmpty()) {
            throw new ResourceNotFoundException(invitationId, Resource.INVITATION);
        }

        Invitation invitation = invitationOpt.get();

        boolean exists = enrollmentRepository.existsByStudentIdAndInvitation_Course_Id(
                student.getId(), invitation.getCourse().getId());
        if (exists) {
            throw new StudentAlreadyEnrolledException(
                    student.getId(), invitation.getCourse().getId());
        }

        Enrollment enrollment = new Enrollment(invitation, student);
        enrollmentRepository.save(enrollment);

        return enrollmentMapper.to(enrollment);
    }
}
