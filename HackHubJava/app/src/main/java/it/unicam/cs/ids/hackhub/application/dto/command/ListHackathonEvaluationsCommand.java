package it.unicam.cs.ids.hackhub.application.dto.command;

import java.util.UUID;

/** Input for retrieving the evaluations of a hackathon. */
public record ListHackathonEvaluationsCommand(
        UUID hackathonId,
        UUID organizerId
) {
}
