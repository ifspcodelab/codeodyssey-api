package app.codeodyssey.codeodysseyapi.invitation.service;

import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class InvitationCleanupService {
    private final InvitationRepository invitationRepository;

    @Scheduled(fixedRateString = "${scheduler.invitation.interval}")
    public void cleanupInvitation (){
        List<Invitation> invitationWhereCourseEnded = invitationRepository.findAllByCourseEndDate(LocalDate.now());

        for (Invitation invitation : invitationWhereCourseEnded){
            if(invitation.isActive()){
                invitation.setActive(false);
                invitationRepository.save(invitation);

                log.info("Invitation with id " + invitation.getId() + " set to deactivated due to end of course");
            }
        }

        invitationWhereCourseEnded.clear();
    }

}
