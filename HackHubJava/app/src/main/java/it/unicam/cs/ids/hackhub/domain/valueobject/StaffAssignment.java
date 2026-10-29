package it.unicam.cs.ids.hackhub.domain.valueobject;

import it.unicam.cs.ids.hackhub.domain.enums.StaffRole;

import java.util.Objects;

/** Associates one user with one staff role in a hackathon. */
public record StaffAssignment(UserId userId, StaffRole role) {
    public StaffAssignment {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(role, "role cannot be null");
    }
}
