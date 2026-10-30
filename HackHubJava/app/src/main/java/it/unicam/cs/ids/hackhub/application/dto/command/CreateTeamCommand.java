package it.unicam.cs.ids.hackhub.application.dto.command;

import java.util.UUID;

/** Application input for creating a team. */
public record CreateTeamCommand(String name, UUID creatorId, String paymentDetails) {
}
