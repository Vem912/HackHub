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

/** In-memory repository for the current team submission. */
@Repository
public class InMemorySubmissionRepository implements SubmissionRepository {
    private final ConcurrentMap<String, Submission> submissions = new ConcurrentHashMap<>();

    @Override
    public Optional<Submission> findByHackathonAndTeam(HackathonId hackathonId, TeamId teamId) {
        return Optional.ofNullable(submissions.get(key(hackathonId, teamId)));
    }

    @Override
    public Optional<Submission> findById(UUID submissionId) {
        return submissions.values().stream()
                .filter(submission -> submission.id().equals(submissionId))
                .findFirst();
    }

    @Override
    public List<Submission> findByHackathon(HackathonId hackathonId) {
        return submissions.values().stream()
                .filter(submission -> submission.hackathonId().equals(hackathonId))
                .toList();
    }

    @Override
    public void save(Submission submission) {
        submissions.put(key(submission.hackathonId(), submission.teamId()), submission);
    }

    private String key(HackathonId hackathonId, TeamId teamId) {
        return hackathonId.value() + ":" + teamId.value();
    }
}
