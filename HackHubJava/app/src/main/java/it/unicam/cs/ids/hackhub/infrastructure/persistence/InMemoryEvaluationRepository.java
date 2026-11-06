package it.unicam.cs.ids.hackhub.infrastructure.persistence;

import it.unicam.cs.ids.hackhub.application.port.out.EvaluationRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Evaluation;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** In-memory repository for evaluations, one evaluation per judge and submission. */
@Repository
public class InMemoryEvaluationRepository implements EvaluationRepository {
    private final ConcurrentMap<String, Evaluation> evaluations = new ConcurrentHashMap<>();

    @Override
    public void save(Evaluation evaluation) {
        evaluations.put(key(evaluation.submissionId(), evaluation.judgeId().value()), evaluation);
    }

    @Override
    public long countBySubmissionIds(Iterable<UUID> submissionIds) {
        var ids = new java.util.HashSet<UUID>();
        submissionIds.forEach(ids::add);
        return evaluations.values().stream()
                .map(Evaluation::submissionId)
                .filter(ids::contains)
                .distinct()
                .count();
    }

    private String key(UUID submissionId, UUID judgeId) {
        return submissionId + ":" + judgeId;
    }
}
