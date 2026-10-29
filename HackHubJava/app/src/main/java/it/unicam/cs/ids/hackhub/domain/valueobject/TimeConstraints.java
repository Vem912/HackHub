package it.unicam.cs.ids.hackhub.domain.valueobject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Local schedule for team registration and the hackathon event.
 * The registration deadline must precede the event start, which must precede its end.
 */
public record TimeConstraints(
        LocalDateTime teamRegistrationDeadline,
        LocalDateTime hackathonStart,
        LocalDateTime hackathonEnd
) {
    public TimeConstraints {
        Objects.requireNonNull(teamRegistrationDeadline, "teamRegistrationDeadline cannot be null");
        Objects.requireNonNull(hackathonStart, "hackathonStart cannot be null");
        Objects.requireNonNull(hackathonEnd, "hackathonEnd cannot be null");

        if (!teamRegistrationDeadline.isBefore(hackathonStart)) {
            throw new IllegalArgumentException("Team registration deadline must be before the hackathon start");
        }
        if (!hackathonStart.isBefore(hackathonEnd)) {
            throw new IllegalArgumentException("Hackathon start must be before the hackathon end");
        }
    }
}
