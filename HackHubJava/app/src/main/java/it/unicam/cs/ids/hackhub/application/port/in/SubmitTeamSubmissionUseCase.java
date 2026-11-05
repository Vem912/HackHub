package it.unicam.cs.ids.hackhub.application.port.in;

import it.unicam.cs.ids.hackhub.application.dto.command.SubmitTeamSubmissionCommand;
import it.unicam.cs.ids.hackhub.application.dto.result.SubmitTeamSubmissionResult;

/** Inbound application port for creating or replacing a team submission. */
public interface SubmitTeamSubmissionUseCase {
    SubmitTeamSubmissionResult submit(SubmitTeamSubmissionCommand command);
}
