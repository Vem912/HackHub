package it.unicam.cs.ids.hackhub.application.dto.result;

import java.util.UUID;

/** Application output for a successful team registration. */
public record RegisterTeamToHackathonResult(UUID teamId, UUID hackathonId) {
}
