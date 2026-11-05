package it.unicam.cs.ids.hackhub.application.dto.command;

import it.unicam.cs.ids.hackhub.domain.valueobject.SubmissionFile;

import java.util.UUID;

/** Input for creating or replacing a team's submission. */
public record SubmitTeamSubmissionCommand(
        UUID userId,
        UUID teamId,
        UUID hackathonId,
        String title,
        String description,
        SubmissionFile file
) {
}
