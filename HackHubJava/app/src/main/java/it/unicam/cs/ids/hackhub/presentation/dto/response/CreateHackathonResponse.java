package it.unicam.cs.ids.hackhub.presentation.dto.response;

import java.util.UUID;

/** HTTP response identifying the hackathon created by the request. */
public record CreateHackathonResponse(UUID id, String name) {
}
