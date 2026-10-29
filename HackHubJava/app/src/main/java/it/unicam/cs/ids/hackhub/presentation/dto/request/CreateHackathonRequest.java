package it.unicam.cs.ids.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** HTTP payload for creating a hackathon; identifiers are represented as strings. */
public record CreateHackathonRequest(
        @NotBlank String name,
        @NotBlank String location,
        @NotNull @PositiveOrZero BigDecimal prize,
        @NotBlank String organizerId,
        @NotBlank String judgeId,
        @NotNull List<@NotBlank String> mentorIds,
        @NotNull LocalDateTime teamRegistrationDeadline,
        @NotNull LocalDateTime hackathonStart,
        @NotNull LocalDateTime hackathonEnd
) {
    public CreateHackathonRequest {
        mentorIds = mentorIds == null ? null : List.copyOf(mentorIds);
    }
}
