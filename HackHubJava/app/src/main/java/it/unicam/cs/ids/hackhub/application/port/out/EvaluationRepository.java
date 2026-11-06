package it.unicam.cs.ids.hackhub.application.port.out;

import it.unicam.cs.ids.hackhub.domain.entity.Evaluation;

import java.util.UUID;

/** Persistence port for submission evaluations. */
public interface EvaluationRepository {
    void save(Evaluation evaluation);

    long countBySubmissionIds(Iterable<UUID> submissionIds);
}
