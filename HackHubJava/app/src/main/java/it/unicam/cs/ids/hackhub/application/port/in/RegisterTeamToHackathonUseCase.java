package it.unicam.cs.ids.hackhub.application.port.in;

import it.unicam.cs.ids.hackhub.application.dto.command.RegisterTeamToHackathonCommand;
import it.unicam.cs.ids.hackhub.application.dto.result.RegisterTeamToHackathonResult;

/** Inbound application port for registering a team in a hackathon. */
public interface RegisterTeamToHackathonUseCase {
    RegisterTeamToHackathonResult register(RegisterTeamToHackathonCommand command);
}
