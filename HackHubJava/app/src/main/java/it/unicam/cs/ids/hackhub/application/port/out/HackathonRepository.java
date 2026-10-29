package it.unicam.cs.ids.hackhub.application.port.out;

import it.unicam.cs.ids.hackhub.domain.entity.Hackathon;

/** Outbound persistence port used by the application to store hackathons. */
public interface HackathonRepository {
    /** Stores the supplied hackathon. */
    void save(Hackathon hackathon);
}
