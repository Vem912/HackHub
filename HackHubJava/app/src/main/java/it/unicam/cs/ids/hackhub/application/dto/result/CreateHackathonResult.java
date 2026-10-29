package it.unicam.cs.ids.hackhub.application.dto.result;

import java.util.UUID;

/** Application output identifying the hackathon created by the use case. */
public record CreateHackathonResult(UUID id, String name) {
}
