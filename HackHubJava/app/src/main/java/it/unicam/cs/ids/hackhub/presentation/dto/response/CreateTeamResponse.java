package it.unicam.cs.ids.hackhub.presentation.dto.response;

import java.util.UUID;

/** HTTP response identifying the team created by the request. */
public record CreateTeamResponse(UUID id, String name) {
}
