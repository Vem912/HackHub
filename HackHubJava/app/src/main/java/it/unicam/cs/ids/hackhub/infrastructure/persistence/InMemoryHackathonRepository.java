package it.unicam.cs.ids.hackhub.infrastructure.persistence;

import it.unicam.cs.ids.hackhub.application.port.out.HackathonRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Hackathon;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Optional;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;

/**
 * In-memory adapter for the hackathon persistence port.
 *
 * <p>Hackathons are indexed by their UUID. This implementation is intended for
 * development and tests: all data is lost when the application stops.</p>
 */
@Repository
public class InMemoryHackathonRepository implements HackathonRepository {
    private final ConcurrentMap<UUID, Hackathon> hackathons = new ConcurrentHashMap<>();

    /** Stores a hackathon, replacing the previous value with the same identifier. */
    @Override
    public void save(Hackathon hackathon) {
        hackathons.put(hackathon.id().value(), hackathon);
    }

    /** Finds a hackathon by identifier, returning an empty result when it is absent. */
    @Override
    public Optional<Hackathon> findById(HackathonId hackathonId) {
        return Optional.ofNullable(hackathons.get(hackathonId.value()));
    }
}
