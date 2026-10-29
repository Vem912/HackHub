package it.unicam.cs.ids.hackhub.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/** Strongly typed identifier for a user. */
public record UserId(UUID value) {
    public UserId {
        Objects.requireNonNull(value, "value cannot be null");
    }
}
