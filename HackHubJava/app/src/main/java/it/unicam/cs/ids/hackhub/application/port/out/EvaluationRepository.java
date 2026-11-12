package it.unicam.cs.ids.hackhub.application.port.out;

import it.unicam.cs.ids.hackhub.domain.entity.Evaluation;

import java.util.List;
import java.util.UUID;

/** Persistence port for submission evaluations. */
public interface EvaluationRepository {
    void save(Evaluation evaluation);

    Evaluation findEvaluation(UUID submissionId);

    List<Evaluation> findAllBySubmissionIds(Iterable<UUID> submissionIds);

    void deleteEvaluation(Evaluation evaluation);

    void editEvaluation(Evaluation evaluation);
}
