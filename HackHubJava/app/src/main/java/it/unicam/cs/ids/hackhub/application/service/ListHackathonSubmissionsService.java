package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.result.SubmissionResult;
import it.unicam.cs.ids.hackhub.application.exception.HackathonNotFoundException;
import it.unicam.cs.ids.hackhub.application.port.in.ListHackathonSubmissionsUseCase;
import it.unicam.cs.ids.hackhub.application.port.out.HackathonRepository;
import it.unicam.cs.ids.hackhub.application.port.out.SubmissionRepository;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/** Retrieves complete submission details for a hackathon's judging view. */
@Service
public class ListHackathonSubmissionsService implements ListHackathonSubmissionsUseCase {
    private final HackathonRepository hackathonRepository;
    private final SubmissionRepository submissionRepository;

    public ListHackathonSubmissionsService(
            HackathonRepository hackathonRepository,
            SubmissionRepository submissionRepository
    ) {
        this.hackathonRepository = Objects.requireNonNull(hackathonRepository);
        this.submissionRepository = Objects.requireNonNull(submissionRepository);
    }

    @Override
    public java.util.List<SubmissionResult> list(UUID hackathonId) {
        var id = new HackathonId(Objects.requireNonNull(hackathonId, "hackathonId cannot be null"));
        hackathonRepository.findById(id).orElseThrow(HackathonNotFoundException::new);
        return submissionRepository.findByHackathon(id).stream()
                .map(submission -> new SubmissionResult(
                        submission.id(),
                        submission.teamId().value(),
                        submission.title(),
                        submission.description(),
                        submission.file().fileName(),
                        submission.file().content(),
                        submission.submittedAt()))
                .toList();
    }
}
