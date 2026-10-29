package it.unicam.cs.ids.hackhub.application.dto.command;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/** Application input for creating a hackathon, independent of the HTTP request format. */
public record CreateHackathonCommand(
        String name,
        String location,
        BigDecimal prize,
        UUID organizerId,
        UUID judgeId,
        List<UUID> mentorIds,
        LocalDateTime teamRegistrationDeadline,
        LocalDateTime hackathonStart,
        LocalDateTime hackathonEnd
) {
    public CreateHackathonCommand {
        mentorIds = List.copyOf(mentorIds);
    }
}
