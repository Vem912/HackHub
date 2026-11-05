package it.unicam.cs.ids.hackhub.domain.entity;

import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;
import it.unicam.cs.ids.hackhub.domain.valueobject.SubmissionFile;
import it.unicam.cs.ids.hackhub.domain.valueobject.TeamId;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/** The current submission of one team for one hackathon. */
public record Submission(
        UUID id,
        HackathonId hackathonId,
        TeamId teamId,
        String title,
        String description,
        SubmissionFile file,
        LocalDateTime submittedAt
) {
    public Submission {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(hackathonId, "hackathonId cannot be null");
        Objects.requireNonNull(teamId, "teamId cannot be null");
        title = requireText(title, "title");
        description = requireText(description, "description");
        Objects.requireNonNull(file, "file cannot be null");
        Objects.requireNonNull(submittedAt, "submittedAt cannot be null");
    }

    public static Submission create(
            HackathonId hackathonId,
            TeamId teamId,
            String title,
            String description,
            SubmissionFile file,
            LocalDateTime submittedAt
    ) {
        return new Submission(UUID.randomUUID(), hackathonId, teamId, title, description, file, submittedAt);
    }

    private static String requireText(String value, String field) {
        Objects.requireNonNull(value, field + " cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return value;
    }
}
