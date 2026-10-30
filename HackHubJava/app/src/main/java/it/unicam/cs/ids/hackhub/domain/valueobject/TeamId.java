package it.unicam.cs.ids.hackhub.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/** Strongly typed identifier for a team. */
public record TeamId(UUID value) {
    public TeamId {
        Objects.requireNonNull(value, "value cannot be null");
    }

    /** Generates a new identifier using a random UUID. */
    public static TeamId generate() {
        return new TeamId(UUID.randomUUID());
    }
}
