package it.unicam.cs.ids.hackhub.application.dto.result;

import java.util.UUID;

/** Application output identifying the team created by the use case. */
public record CreateTeamResult(UUID id, String name) {
}
