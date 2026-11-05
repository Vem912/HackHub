package it.unicam.cs.ids.hackhub.infrastructure.persistence;

import it.unicam.cs.ids.hackhub.application.port.out.HackathonRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Hackathon;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Optional;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;

/** In-memory repository adapter; stored data is lost when the application stops. */
@Repository
public class InMemoryHackathonRepository implements HackathonRepository {
    private final ConcurrentMap<UUID, Hackathon> hackathons = new ConcurrentHashMap<>();

    @Override
    public void save(Hackathon hackathon) {
        hackathons.put(hackathon.id().value(), hackathon);
    }

    @Override
    public Optional<Hackathon> findById(HackathonId hackathonId) {
        return Optional.ofNullable(hackathons.get(hackathonId.value()));
    }
}
