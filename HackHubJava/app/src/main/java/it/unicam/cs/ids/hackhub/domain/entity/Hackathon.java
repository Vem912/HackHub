package it.unicam.cs.ids.hackhub.domain.entity;

import it.unicam.cs.ids.hackhub.domain.enums.HackathonState;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;
import it.unicam.cs.ids.hackhub.domain.valueobject.StaffStructure;
import it.unicam.cs.ids.hackhub.domain.valueobject.TimeConstraints;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Aggregate root for a hackathon, its staff, schedule, prize and lifecycle state.
 */
public class Hackathon {

    private final HackathonId id;
    private final StaffStructure staffStructure;
    private final TimeConstraints timeConstraints;
    private HackathonState state;
    private final String name;
    private final String location;
    private final BigDecimal prize;

    private Hackathon(
            HackathonId id,
            String name,
            String location,
            BigDecimal prize,
            StaffStructure staffStructure,
            TimeConstraints timeConstraints
    ) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.name = requireText(name, "name");
        this.location = requireText(location, "location");
        this.prize = Objects.requireNonNull(prize, "prize cannot be null");
        if (prize.signum() < 0) {
            throw new IllegalArgumentException("prize cannot be negative");
        }
        this.staffStructure = Objects.requireNonNull(staffStructure, "staffStructure cannot be null");
        this.timeConstraints = Objects.requireNonNull(timeConstraints, "timeConstraints cannot be null");
        this.state = HackathonState.REGISTRATION_OPEN;
    }

    /**
     * Creates a hackathon with a generated identifier and registration-open state.
     *
     * @throws IllegalArgumentException if the name or location is blank, or the prize is negative
     * @throws NullPointerException if a required value is null
     */
    public static Hackathon create(
            String name,
            String location,
            BigDecimal prize,
            StaffStructure staffStructure,
            TimeConstraints timeConstraints
    ) {
        return new Hackathon(HackathonId.generate(), name, location, prize, staffStructure, timeConstraints);
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return value;
    }

    /** Returns the stable identifier of this hackathon. */
    public HackathonId id() {
        return id;
    }

    /** Returns the hackathon name. */
    public String name() {
        return name;
    }

    /** Returns the hackathon location. */
    public String location() {
        return location;
    }

    /** Returns the prize amount. */
    public BigDecimal prize() {
        return prize;
    }

    /** Returns the staff assignments associated with this hackathon. */
    public StaffStructure staffStructure() {
        return staffStructure;
    }

    /** Returns the registration deadline and event start/end times. */
    public TimeConstraints timeConstraints() {
        return timeConstraints;
    }

    /** Returns the current lifecycle state. */
    public HackathonState state() {
        return state;
    }

    /**
     * Changes the lifecycle state.
     *
     * @param state new state; must not be null
     */
    public void changeState(HackathonState state) {
        this.state = Objects.requireNonNull(state, "state cannot be null");
    }
}
