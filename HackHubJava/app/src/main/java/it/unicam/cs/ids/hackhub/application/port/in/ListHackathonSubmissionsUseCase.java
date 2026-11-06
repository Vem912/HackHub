package it.unicam.cs.ids.hackhub.application.port.in;

import it.unicam.cs.ids.hackhub.application.dto.result.SubmissionResult;

import java.util.List;
import java.util.UUID;

/** Inbound application port for retrieving all submissions of a hackathon. */
public interface ListHackathonSubmissionsUseCase {
    List<SubmissionResult> list(UUID hackathonId);
}
