package it.unicam.cs.ids.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.AssertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/** HTTP payload for creating a hackathon; identifiers are represented as strings. */
public record CreateHackathonRequest(
        @NotBlank String name,
        @NotBlank String location,
        @NotNull @PositiveOrZero @Digits(integer = 19, fraction = 2) BigDecimal prize,
        @Positive int maxTeamMembers,
        @NotNull UUID organizerId,
        @NotNull UUID judgeId,
        @NotNull List<@NotNull UUID> mentorIds,
        @NotNull LocalDateTime teamRegistrationDeadline,
        @NotNull LocalDateTime hackathonStart,
        @NotNull LocalDateTime hackathonEnd
) {
    public CreateHackathonRequest {
        mentorIds = mentorIds == null ? null : List.copyOf(mentorIds);
    }

//    @AssertTrue(message = "teamRegistrationDeadline must be before hackathonStart, which must be before hackathonEnd")
//    public boolean hasValidSchedule() {
//        return teamRegistrationDeadline != null
//                && hackathonStart != null
//                && hackathonEnd != null
//                && teamRegistrationDeadline.isBefore(hackathonStart)
//                && hackathonStart.isBefore(hackathonEnd);
//    }
}
