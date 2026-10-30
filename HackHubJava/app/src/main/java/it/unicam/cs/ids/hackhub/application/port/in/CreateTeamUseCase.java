package it.unicam.cs.ids.hackhub.application.port.in;

import it.unicam.cs.ids.hackhub.application.dto.command.CreateTeamCommand;
import it.unicam.cs.ids.hackhub.application.dto.result.CreateTeamResult;

/** Inbound application port for creating a team. */
public interface CreateTeamUseCase {
    /** Creates and persists a team from the supplied application command. */
    CreateTeamResult create(CreateTeamCommand command);
}
