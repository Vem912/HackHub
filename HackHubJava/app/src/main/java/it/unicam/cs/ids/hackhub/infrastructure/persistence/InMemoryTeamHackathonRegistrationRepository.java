package it.unicam.cs.ids.hackhub.infrastructure.persistence;

import it.unicam.cs.ids.hackhub.application.port.out.TeamHackathonRegistrationRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Hackathon;
import it.unicam.cs.ids.hackhub.domain.enums.HackathonState;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;
import it.unicam.cs.ids.hackhub.domain.valueobject.TeamId;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * In-memory adapter for team-to-hackathon registrations.
 *
 * <p>It keeps both the registrations for each team and the hackathons needed
 * to determine whether an existing registration is still active. Access is
 * synchronized because both indexes must be updated atomically. All data is
 * lost when the application stops.</p>
 */
@Repository
public class InMemoryTeamHackathonRegistrationRepository
        implements TeamHackathonRegistrationRepository {
    private final Map<TeamId, Set<HackathonId>> registrations = new HashMap<>();
    private final Map<HackathonId, Hackathon> hackathons = new HashMap<>();

    /** Returns whether the team is registered for the specified hackathon. */
    @Override
    public synchronized boolean isRegistered(TeamId teamId, HackathonId hackathonId) {
        var registeredHackathons = registrations.get(teamId);
        return registeredHackathons != null && registeredHackathons.contains(hackathonId);
    }

    /** Returns whether the team has a registration in an active hackathon. */
    @Override
    public synchronized boolean hasActiveRegistration(TeamId teamId) {
        var registeredHackathons = registrations.get(teamId);
        return registeredHackathons != null
                && registeredHackathons.stream()
                .map(hackathons::get)
                .anyMatch(hackathon -> hackathon != null && isActive(hackathon.state()));
    }

    /**
     * Registers a team if it is not already registered and has no other active
     * registration.
     *
     * @return {@code true} when the registration was created, otherwise {@code false}
     */
    @Override
    public synchronized boolean registerIfAvailable(TeamId teamId, Hackathon hackathon) {
        if (isRegistered(teamId, hackathon.id()) || hasActiveRegistration(teamId)) {
            return false;
        }
        registrations.computeIfAbsent(teamId, ignored -> new HashSet<>()).add(hackathon.id());
        hackathons.put(hackathon.id(), hackathon);
        return true;
    }

    /** Returns whether a hackathon state prevents another active registration. */
    private boolean isActive(HackathonState state) {
        return state == HackathonState.REGISTRATION_OPEN
                || state == HackathonState.IN_PROGRESS;
    }
}
