package it.unicam.cs.ids.hackhub.application.dto.command;

import java.util.UUID;

/** Input for completing the evaluation phase of a hackathon. */
public record CompleteHackathonEvaluationCommand(
        UUID hackathonId,
        UUID judgeId
) {
}
