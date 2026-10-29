package it.unicam.cs.ids.hackhub.application.port.in;

import it.unicam.cs.ids.hackhub.application.dto.command.CreateHackathonCommand;
import it.unicam.cs.ids.hackhub.application.dto.result.CreateHackathonResult;

/** Inbound application port for creating a hackathon. */
public interface CreateHackathonUseCase {

    /** Creates and persists a hackathon from the supplied application command. */
    CreateHackathonResult create (CreateHackathonCommand createHackathonCommand);
}
