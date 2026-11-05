package it.unicam.cs.ids.hackhub.presentation.dto.response;

import java.util.UUID;

/** HTTP response for a successful team registration. */
public record RegisterTeamToHackathonResponse(UUID teamId, UUID hackathonId) {
}
