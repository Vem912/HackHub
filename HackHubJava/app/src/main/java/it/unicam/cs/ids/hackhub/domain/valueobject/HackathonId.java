package it.unicam.cs.ids.hackhub.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/** Strongly typed identifier for a hackathon. */
public record HackathonId(UUID value) {
    public HackathonId {
        Objects.requireNonNull(value, "value cannot be null");
    }

    /** Generates a new identifier using a random UUID. */
    public static HackathonId generate() {
        return new HackathonId(UUID.randomUUID());
    }
}
