package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.CreateTeamCommand;
import it.unicam.cs.ids.hackhub.application.dto.result.CreateTeamResult;
import it.unicam.cs.ids.hackhub.application.exception.UserAlreadyInTeamException;
import it.unicam.cs.ids.hackhub.application.port.in.CreateTeamUseCase;
import it.unicam.cs.ids.hackhub.application.port.out.TeamRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Team;
import it.unicam.cs.ids.hackhub.domain.valueobject.PaymentDetails;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import org.springframework.stereotype.Service;

import java.util.Objects;

/** Implements the create-team use case by building and persisting the domain model. */
@Service
public class CreateTeamService implements CreateTeamUseCase {
    private final TeamRepository teamRepository;

    public CreateTeamService(TeamRepository teamRepository) {
        this.teamRepository = Objects.requireNonNull(teamRepository);
    }

    @Override
    public CreateTeamResult create(CreateTeamCommand command) {
        Objects.requireNonNull(command, "command cannot be null");
        var creatorId = new UserId(command.creatorId());
        var team = Team.create(
                command.name(),
                creatorId,
                new PaymentDetails(command.paymentDetails()));

        if (!teamRepository.saveIfMembersAvailable(team)) {
            throw new UserAlreadyInTeamException();
        }

        return new CreateTeamResult(team.id().value(), team.name());
    }
}
