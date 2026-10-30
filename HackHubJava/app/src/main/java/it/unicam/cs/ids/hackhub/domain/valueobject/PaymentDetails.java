package it.unicam.cs.ids.hackhub.domain.valueobject;

import java.util.Objects;

/** Free-form details used to receive a team's prize payment. */
public record PaymentDetails(String value) {
    public PaymentDetails {
        Objects.requireNonNull(value, "value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("value cannot be blank");
        }
    }
}
