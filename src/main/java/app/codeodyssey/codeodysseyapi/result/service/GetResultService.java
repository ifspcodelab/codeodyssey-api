package app.codeodyssey.codeodysseyapi.result.service;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.activity.data.ActivityRepository;
import app.codeodyssey.codeodysseyapi.common.exception.EmailNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenAccessException;
import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.enrollment.data.Enrollment;
import app.codeodyssey.codeodysseyapi.resolution.data.Resolution;
import app.codeodyssey.codeodysseyapi.resolution.data.ResolutionRepository;
import app.codeodyssey.codeodysseyapi.result.api.ResultResponse;
import app.codeodyssey.codeodysseyapi.result.data.Result;
import app.codeodyssey.codeodysseyapi.result.data.ResultRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetResultService {

    private final ResultRepository resultRepository;
    private final ResultMapper resultMapper;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ResolutionRepository resolutionRepository;

    public ResultResponse execute(UUID activityId, UUID resolutionId, String email) {

        Activity activity = this.activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException(activityId, Resource.ACTIVITY));

        Course course = activity.getCourse();

        Resolution resolution = this.resolutionRepository.findById(resolutionId)
                .orElseThrow(() -> new ResourceNotFoundException(resolutionId, Resource.RESOLUTION));

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email));

        if (!user.getRole().equals(UserRole.ADMIN)) {

            for (Enrollment e : user.getEnrollments()) {
                if (e.getInvitation().getCourse().getId().equals(course.getId()) &&
                        resolution.getStudent().getId().equals(user.getId())) {
                    Result result = resultRepository.findByResolution(resolution)
                            .orElseThrow(() -> new ResourceNotFoundException(null, Resource.RESULT));
                    return resultMapper.to(result);
                }
            }

            if (course.getProfessor().getId().equals(user.getId())) {
                Result result = resultRepository.findByResolution(resolution)
                        .orElseThrow(() -> new ResourceNotFoundException(null, Resource.RESULT));
                return resultMapper.to(result);
            }

            throw new ForbiddenAccessException(user.getId());
        }

        Result result = resultRepository.findByResolution(resolution)
                .orElseThrow(() -> new ResourceNotFoundException(null, Resource.RESULT));
        return resultMapper.to(result);
    }
}
