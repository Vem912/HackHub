package it.unicam.cs.ids.hackhub.domain.valueobject;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;

public class TimeConstraintsTest {
    @Test
    public void acceptsDeadlineBeforeHackathonStartAndEnd() {
        new TimeConstraints(
                LocalDateTime.of(2026, 5, 1, 23, 59),
                LocalDateTime.of(2026, 5, 2, 9, 0),
                LocalDateTime.of(2026, 5, 3, 18, 0)
        );
    }

    @Test
    public void rejectsDeadlineAtOrAfterHackathonStart() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 2, 9, 0);

        assertThrows(IllegalArgumentException.class, () -> new TimeConstraints(
                start,
                start,
                start.plusDays(1)
        ));
    }

    @Test
    public void rejectsHackathonEndAtOrBeforeStart() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 2, 9, 0);

        assertThrows(IllegalArgumentException.class, () -> new TimeConstraints(
                start.minusDays(1),
                start,
                start
        ));
    }
}
