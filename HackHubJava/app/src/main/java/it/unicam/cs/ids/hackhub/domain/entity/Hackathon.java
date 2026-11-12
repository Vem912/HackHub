package it.unicam.cs.ids.hackhub.domain.entity;

import it.unicam.cs.ids.hackhub.domain.enums.HackathonState;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;
import it.unicam.cs.ids.hackhub.domain.valueobject.StaffStructure;
import it.unicam.cs.ids.hackhub.domain.valueobject.TeamId;
import it.unicam.cs.ids.hackhub.domain.valueobject.TimeConstraints;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
    private final int maxTeamMembers;
    private TeamId winnerTeamId;
    private boolean evaluationCompleted;

    private Hackathon(
            HackathonId id,
            String name,
            String location,
            BigDecimal prize,
            int maxTeamMembers,
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
        if (maxTeamMembers <= 0) {
            throw new IllegalArgumentException("maxTeamMembers must be positive");
        }
        this.maxTeamMembers = maxTeamMembers;
        this.staffStructure = Objects.requireNonNull(staffStructure, "staffStructure cannot be null");
        this.timeConstraints = Objects.requireNonNull(timeConstraints, "timeConstraints cannot be null");
        this.state = HackathonState.REGISTRATION_OPEN;
        winnerTeamId = null;
        evaluationCompleted = false;
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
            int maxTeamMembers,
            StaffStructure staffStructure,
            TimeConstraints timeConstraints
    ) {
        return new Hackathon(HackathonId.generate(), name, location, prize, maxTeamMembers, staffStructure, timeConstraints);
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

    public int maxTeamMembers() {
        return maxTeamMembers;
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

    /** Returns the identifier of the winning team, if any. */
    public Optional<TeamId> winnerTeamId() {
        return Optional.ofNullable(winnerTeamId);
    }

    /**
     * Changes the lifecycle state.
     *
     * @param state new state; must not be null
     */
    public void changeState(HackathonState state) {
        this.state = Objects.requireNonNull(state, "state cannot be null");
    }

    /**
     * Concludes the hackathon with a winning team.
     *
     * @param winnerTeamId the identifier of the winning team; must not be null
     * @throws IllegalStateException if the hackathon is already concluded or evaluation is not completed
     */
    public void concludeWithWinner(TeamId winnerTeamId) {
        Objects.requireNonNull(winnerTeamId);
        conclude(winnerTeamId);
    }

    /**
     * Concludes the hackathon without a winning team.
     *
     * @throws IllegalStateException if the hackathon is already concluded or evaluation is not completed
     */
    public void concludeWithoutWinner() {
        conclude(null);
    }

    /**
     * Concludes the hackathon.
     *
     * @param winnerTeamId the identifier of the winning team, or null if no winner
     * @throws IllegalStateException if the hackathon is already concluded or evaluation is not completed
     */
    private void conclude(TeamId winnerTeamId) {
        if (state == HackathonState.CONCLUDED) {
            throw new IllegalStateException("Hackathon is already concluded");
        }
        if (!evaluationCompleted) {
            throw new IllegalStateException("Cannot conclude hackathon without completing evaluation");
        }

        this.winnerTeamId = winnerTeamId;
        this.state = HackathonState.CONCLUDED;
    }

    /**
     * Returns true if the evaluation phase is completed, false otherwise.
     */
    public boolean isEvaluationCompleted() {
        return evaluationCompleted;
    }

    /**
     * Completes the evaluation phase.
     */
    public void completeEvaluation() {
        if (state != HackathonState.UNDER_EVALUATION) {
            throw new IllegalStateException(
                    "The hackathon is not under evaluation.");
        }

        evaluationCompleted = true;
    }
}
