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

/** In-memory registration adapter; stored data is lost when the application stops. */
@Repository
public class InMemoryTeamHackathonRegistrationRepository
        implements TeamHackathonRegistrationRepository {
    private final Map<TeamId, Set<HackathonId>> registrations = new HashMap<>();
    private final Map<HackathonId, Hackathon> hackathons = new HashMap<>();

    @Override
    public synchronized boolean isRegistered(TeamId teamId, HackathonId hackathonId) {
        var registeredHackathons = registrations.get(teamId);
        return registeredHackathons != null && registeredHackathons.contains(hackathonId);
    }

    @Override
    public synchronized boolean hasActiveRegistration(TeamId teamId) {
        var registeredHackathons = registrations.get(teamId);
        return registeredHackathons != null
                && registeredHackathons.stream()
                .map(hackathons::get)
                .anyMatch(hackathon -> hackathon != null && isActive(hackathon.state()));
    }

    @Override
    public synchronized boolean registerIfAvailable(TeamId teamId, Hackathon hackathon) {
        if (isRegistered(teamId, hackathon.id()) || hasActiveRegistration(teamId)) {
            return false;
        }
        registrations.computeIfAbsent(teamId, ignored -> new HashSet<>()).add(hackathon.id());
        hackathons.put(hackathon.id(), hackathon);
        return true;
    }

    private boolean isActive(HackathonState state) {
        return state == HackathonState.REGISTRATION_OPEN
                || state == HackathonState.IN_PROGRESS;
    }
}
