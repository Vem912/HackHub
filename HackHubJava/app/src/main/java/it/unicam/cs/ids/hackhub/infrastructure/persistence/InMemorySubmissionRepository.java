package it.unicam.cs.ids.hackhub.infrastructure.persistence;

import it.unicam.cs.ids.hackhub.application.port.out.SubmissionRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Submission;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;
import it.unicam.cs.ids.hackhub.domain.valueobject.TeamId;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory adapter for team submissions.
 *
 * <p>Submissions are indexed by the hackathon/team pair, so saving a new
 * submission for the same pair replaces the current one. All data is lost
 * when the application stops.</p>
 */
@Repository
public class InMemorySubmissionRepository implements SubmissionRepository {
    private final ConcurrentMap<String, Submission> submissions = new ConcurrentHashMap<>();

    /** Finds the current submission for a team in a hackathon. */
    @Override
    public Optional<Submission> findByHackathonAndTeam(HackathonId hackathonId, TeamId teamId) {
        return Optional.ofNullable(submissions.get(key(hackathonId, teamId)));
    }

    /** Finds a submission by its identifier. */
    @Override
    public Optional<Submission> findById(UUID submissionId) {
        return submissions.values().stream()
                .filter(submission -> submission.id().equals(submissionId))
                .findFirst();
    }

    /** Returns all submissions belonging to the specified hackathon. */
    @Override
    public List<Submission> findByHackathon(HackathonId hackathonId) {
        return submissions.values().stream()
                .filter(submission -> submission.hackathonId().equals(hackathonId))
                .toList();
    }

    /** Stores the current submission for its hackathon/team pair. */
    @Override
    public void save(Submission submission) {
        submissions.put(key(submission.hackathonId(), submission.teamId()), submission);
    }

    /** Builds the composite key used to index a team submission. */
    private String key(HackathonId hackathonId, TeamId teamId) {
        return hackathonId.value() + ":" + teamId.value();
    }
}
