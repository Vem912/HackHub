package it.unicam.cs.ids.hackhub.application.port.out;

import it.unicam.cs.ids.hackhub.domain.entity.Hackathon;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;
import it.unicam.cs.ids.hackhub.domain.valueobject.TeamId;

/** Persistence port for team registrations in hackathons. */
public interface TeamHackathonRegistrationRepository {
    boolean isRegistered(TeamId teamId, HackathonId hackathonId);

    boolean hasActiveRegistration(TeamId teamId);

    /**
     * Registers the team only if it is not already registered and has no active registration.
     * The checks and insertion are one atomic operation.
     */
    boolean registerIfAvailable(TeamId teamId, Hackathon hackathon);
}
