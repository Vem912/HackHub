package it.unicam.cs.ids.hackhub.application.port.out;

import it.unicam.cs.ids.hackhub.domain.entity.Hackathon;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;

import java.util.Optional;

/** Outbound persistence port used by the application to store hackathons. */
public interface HackathonRepository {
    /** Stores the supplied hackathon. */
    void save(Hackathon hackathon);

    Optional<Hackathon> findById(HackathonId hackathonId);
}
