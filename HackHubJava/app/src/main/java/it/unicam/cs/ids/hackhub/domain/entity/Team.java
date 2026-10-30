package it.unicam.cs.ids.hackhub.domain.entity;

import it.unicam.cs.ids.hackhub.domain.valueobject.PaymentDetails;
import it.unicam.cs.ids.hackhub.domain.valueobject.TeamId;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;

import java.util.List;
import java.util.Objects;

/** Aggregate root for a team and its prize payment details. */
public class Team {
    private final TeamId id;
    private final String name;
    private final List<UserId> members;
    private PaymentDetails paymentDetails;

    private Team(TeamId id, String name, UserId creatorId, PaymentDetails paymentDetails) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.name = requireText(name);
        this.members = List.of(Objects.requireNonNull(creatorId, "creatorId cannot be null"));
        this.paymentDetails = Objects.requireNonNull(paymentDetails, "paymentDetails cannot be null");
    }

    /** Creates a team with a generated identifier and the creator as its first member. */
    public static Team create(String name, UserId creatorId, PaymentDetails paymentDetails) {
        return new Team(TeamId.generate(), name, creatorId, paymentDetails);
    }

    private static String requireText(String value) {
        Objects.requireNonNull(value, "name" + " cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("name" + " cannot be blank");
        }
        return value;
    }

    public TeamId id() {
        return id;
    }

    public String name() {
        return name;
    }

    /** Returns the immutable list of team members. */
    public List<UserId> members() {
        return members;
    }

    public PaymentDetails paymentDetails() {
        return paymentDetails;
    }

    /** Replaces the details used to receive the team's prize payment. */
    public void changePaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = Objects.requireNonNull(paymentDetails, "paymentDetails cannot be null");
    }
}
