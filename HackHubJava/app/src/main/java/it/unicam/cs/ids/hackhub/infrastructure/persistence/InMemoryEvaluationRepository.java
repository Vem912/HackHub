package it.unicam.cs.ids.hackhub.infrastructure.persistence;

import it.unicam.cs.ids.hackhub.application.port.out.EvaluationRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Evaluation;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory adapter for submission evaluations.
 *
 * <p>Evaluations are indexed by submission and judge, allowing one evaluation
 * per judge for each submission. All data is lost when the application stops.</p>
 */
@Repository
public class InMemoryEvaluationRepository implements EvaluationRepository {
    /** The in-memory storage for evaluations. */
    private final ConcurrentMap<UUID, Evaluation> evaluations = new ConcurrentHashMap<>();

    /** Stores or replaces the evaluation made by a judge for a submission. */
    @Override
    public void save(Evaluation evaluation) {
        evaluations.put(evaluation.submissionId(), evaluation);
    }

    @Override
    public Evaluation findEvaluation(UUID submissionId) {
        return evaluations.get(submissionId);
    }

    @Override
    public List<Evaluation> findAllBySubmissionIds(Iterable<UUID> submissionIds) {
        var ids = new HashSet<UUID>();
        submissionIds.forEach(ids::add);

        return evaluations.values().stream()
                .filter(evaluation -> ids.contains(evaluation.submissionId()))
                .toList();
    }

    @Override
    public void deleteEvaluation(Evaluation evaluation) {
        evaluations.remove(evaluation.submissionId());
    }

    @Override
    public void editEvaluation(Evaluation evaluation) {
        evaluations.put(evaluation.submissionId(), evaluation);
    }
}
