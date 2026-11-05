package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.SubmitTeamSubmissionCommand;
import it.unicam.cs.ids.hackhub.application.dto.result.SubmitTeamSubmissionResult;
import it.unicam.cs.ids.hackhub.application.exception.HackathonNotFoundException;
import it.unicam.cs.ids.hackhub.application.exception.HackathonNotInProgressException;
import it.unicam.cs.ids.hackhub.application.exception.SubmissionDeadlinePassedException;
import it.unicam.cs.ids.hackhub.application.exception.TeamMemberNotAuthorizedException;
import it.unicam.cs.ids.hackhub.application.exception.TeamNotFoundException;
import it.unicam.cs.ids.hackhub.application.port.in.SubmitTeamSubmissionUseCase;
import it.unicam.cs.ids.hackhub.application.port.out.HackathonRepository;
import it.unicam.cs.ids.hackhub.application.port.out.SubmissionRepository;
import it.unicam.cs.ids.hackhub.application.port.out.TeamRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Submission;
import it.unicam.cs.ids.hackhub.domain.enums.HackathonState;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;
import it.unicam.cs.ids.hackhub.domain.valueobject.TeamId;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

/** Applies the rules for submitting work during the active hackathon window. */
@Service
public class SubmitTeamSubmissionService implements SubmitTeamSubmissionUseCase {
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;
    private final SubmissionRepository submissionRepository;
    private final SubmissionFileValidator fileValidator;
    private final Clock clock;

    @Autowired
    public SubmitTeamSubmissionService(
            TeamRepository teamRepository,
            HackathonRepository hackathonRepository,
            SubmissionRepository submissionRepository
    ) {
        this(teamRepository, hackathonRepository, submissionRepository,
                new SubmissionFileValidator(), Clock.systemDefaultZone());
    }

    public SubmitTeamSubmissionService(
            TeamRepository teamRepository,
            HackathonRepository hackathonRepository,
            SubmissionRepository submissionRepository,
            SubmissionFileValidator fileValidator,
            Clock clock
    ) {
        this.teamRepository = Objects.requireNonNull(teamRepository);
        this.hackathonRepository = Objects.requireNonNull(hackathonRepository);
        this.submissionRepository = Objects.requireNonNull(submissionRepository);
        this.fileValidator = Objects.requireNonNull(fileValidator);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public synchronized SubmitTeamSubmissionResult submit(SubmitTeamSubmissionCommand command) {
        Objects.requireNonNull(command, "command cannot be null");
        var teamId = new TeamId(Objects.requireNonNull(command.teamId(), "teamId cannot be null"));
        var hackathonId = new HackathonId(Objects.requireNonNull(command.hackathonId(), "hackathonId cannot be null"));
        var userId = new UserId(Objects.requireNonNull(command.userId(), "userId cannot be null"));
        var team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        var hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNotFoundException::new);
        var now = LocalDateTime.now(clock);

        if (hackathon.state() != HackathonState.IN_PROGRESS) {
            throw new HackathonNotInProgressException();
        }
        if (now.isAfter(hackathon.timeConstraints().hackathonEnd())) {
            throw new SubmissionDeadlinePassedException();
        }
        if (!team.members().contains(userId)) {
            throw new TeamMemberNotAuthorizedException();
        }

        fileValidator.validate(command.file());
        var previous = submissionRepository.findByHackathonAndTeam(hackathonId, teamId);
        var submission = Submission.create(
                hackathonId, teamId, command.title(), command.description(), command.file(), now);
        submissionRepository.save(submission);
        return new SubmitTeamSubmissionResult(
                submission.id(), teamId.value(), hackathonId.value(),
                submission.file().fileName(), now, previous.isPresent());
    }
}
