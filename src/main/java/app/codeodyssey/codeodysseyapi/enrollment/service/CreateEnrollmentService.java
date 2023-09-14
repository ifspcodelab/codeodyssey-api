package app.codeodyssey.codeodysseyapi.enrollment.service;

import app.codeodyssey.codeodysseyapi.common.exception.*;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CreateEnrollmentService {
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Transactional
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

        if (student.equals(invitation.getCourse().getProfessor())){
            throw new ViolationException(Resource.ENROLLMENT, ViolationType.ENROLLMENT_PROFESSOR_WHO_CREATED_COURSE_CANNOT_BE_ENROLLED, student.getEmail());
        }

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
