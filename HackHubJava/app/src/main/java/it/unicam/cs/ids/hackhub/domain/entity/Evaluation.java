package it.unicam.cs.ids.hackhub.domain.entity;

import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;

import java.util.Objects;
import java.util.UUID;

/** A judge's evaluation of a submission. */
public record Evaluation(
        UUID id,
        UUID submissionId,
        UserId judgeId,
        int score,
        String judgment
) {
    public Evaluation {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(submissionId, "submissionId cannot be null");
        Objects.requireNonNull(judgeId, "judgeId cannot be null");
        if (score < 0 || score > 10) {
            throw new IllegalArgumentException("score must be between 0 and 10");
        }
        judgment = requireText(judgment);
    }

    public static Evaluation create(UUID submissionId, UserId judgeId, int score, String judgment) {
        return new Evaluation(UUID.randomUUID(), submissionId, judgeId, score, judgment);
    }

    private static String requireText(String value) {
        Objects.requireNonNull(value, "judgment" + " cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("judgment" + " cannot be blank");
        }
        return value;
    }
}
