package it.unicam.cs.ids.hackhub.domain.enums;

/** Lifecycle states currently defined for a hackathon. */
public enum HackathonState {
    /** Team registration is open. */
    REGISTRATION_OPEN,
    /** The hackathon event is underway. */
    IN_PROGRESS,
    /** Submitted work is being evaluated. */
    UNDER_EVALUATION,
    /** The hackathon has ended. */
    CONCLUDED
}
