package it.unicam.cs.ids.hackhub.application.port.out;

import it.unicam.cs.ids.hackhub.domain.entity.Submission;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;
import it.unicam.cs.ids.hackhub.domain.valueobject.TeamId;

import java.util.Optional;

/** Persistence port for the current submission of a team. */
public interface SubmissionRepository {
    Optional<Submission> findByHackathonAndTeam(HackathonId hackathonId, TeamId teamId);

    void save(Submission submission);
}
