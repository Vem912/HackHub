package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.RegisterTeamToHackathonCommand;
import it.unicam.cs.ids.hackhub.application.dto.result.RegisterTeamToHackathonResult;
import it.unicam.cs.ids.hackhub.application.exception.HackathonNotFoundException;
import it.unicam.cs.ids.hackhub.application.exception.RegistrationClosedException;
import it.unicam.cs.ids.hackhub.application.exception.TeamAlreadyInActiveHackathonException;
import it.unicam.cs.ids.hackhub.application.exception.TeamAlreadyRegisteredException;
import it.unicam.cs.ids.hackhub.application.exception.TeamMemberIsStaffException;
import it.unicam.cs.ids.hackhub.application.exception.TeamMemberNotAuthorizedException;
import it.unicam.cs.ids.hackhub.application.exception.TeamNotFoundException;
import it.unicam.cs.ids.hackhub.application.exception.TeamSizeLimitExceededException;
import it.unicam.cs.ids.hackhub.application.port.in.RegisterTeamToHackathonUseCase;
import it.unicam.cs.ids.hackhub.application.port.out.HackathonRepository;
import it.unicam.cs.ids.hackhub.application.port.out.TeamHackathonRegistrationRepository;
import it.unicam.cs.ids.hackhub.application.port.out.TeamRepository;
import it.unicam.cs.ids.hackhub.domain.enums.HackathonState;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;
import it.unicam.cs.ids.hackhub.domain.valueobject.StaffAssignment;
import it.unicam.cs.ids.hackhub.domain.valueobject.TeamId;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import org.springframework.stereotype.Service;

import java.util.Objects;

/** Implements the use case for registering a team in an open hackathon. */
@Service
public class RegisterTeamToHackathonService implements RegisterTeamToHackathonUseCase {
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;
    private final TeamHackathonRegistrationRepository registrationRepository;

    public RegisterTeamToHackathonService(
            TeamRepository teamRepository,
            HackathonRepository hackathonRepository,
            TeamHackathonRegistrationRepository registrationRepository) {
        this.teamRepository = Objects.requireNonNull(teamRepository);
        this.hackathonRepository = Objects.requireNonNull(hackathonRepository);
        this.registrationRepository = Objects.requireNonNull(registrationRepository);
    }

    @Override
    public RegisterTeamToHackathonResult register(RegisterTeamToHackathonCommand command) {
        Objects.requireNonNull(command, "command cannot be null");
        var teamId = new TeamId(command.teamId());
        var hackathonId = new HackathonId(command.hackathonId());
        var userId = new UserId(command.userId());
        var team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        var hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNotFoundException::new);

        if (!team.members().contains(userId)) {
            throw new TeamMemberNotAuthorizedException();
        }
        if (hackathon.state() != HackathonState.REGISTRATION_OPEN) {
            throw new RegistrationClosedException();
        }
        if (registrationRepository.isRegistered(teamId, hackathonId)) {
            throw new TeamAlreadyRegisteredException();
        }
        if (registrationRepository.hasActiveRegistration(teamId)) {
            throw new TeamAlreadyInActiveHackathonException();
        }
        if (team.members().size() > hackathon.maxTeamMembers()) {
            throw new TeamSizeLimitExceededException();
        }
        var staffIds = hackathon.staffStructure().assignments().stream()
                .map(StaffAssignment::userId)
                .toList();
        if (team.members().stream().anyMatch(staffIds::contains)) {
            throw new TeamMemberIsStaffException();
        }
        if (!registrationRepository.registerIfAvailable(teamId, hackathon)) {
            throw new TeamAlreadyInActiveHackathonException();
        }
        return new RegisterTeamToHackathonResult(teamId.value(), hackathonId.value());
    }
}
