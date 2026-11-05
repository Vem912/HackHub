package it.unicam.cs.ids.hackhub.application.dto.command;

import java.util.UUID;

/** Application input for registering a team in a hackathon. */
public record RegisterTeamToHackathonCommand(UUID userId, UUID teamId, UUID hackathonId) {
}
