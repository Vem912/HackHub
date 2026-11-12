package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.ListHackathonEvaluationsCommand;
import it.unicam.cs.ids.hackhub.application.dto.result.EvaluationResult;
import it.unicam.cs.ids.hackhub.application.exception.EvaluationsIncompleteException;
import it.unicam.cs.ids.hackhub.application.exception.HackathonNotFoundException;
import it.unicam.cs.ids.hackhub.application.exception.OrganizerNotAuthorizedException;
import it.unicam.cs.ids.hackhub.application.port.in.ListHackathonEvaluationsUseCase;
import it.unicam.cs.ids.hackhub.application.port.out.EvaluationRepository;
import it.unicam.cs.ids.hackhub.application.port.out.HackathonRepository;
import it.unicam.cs.ids.hackhub.application.port.out.SubmissionRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Evaluation;
import it.unicam.cs.ids.hackhub.domain.entity.Submission;
import it.unicam.cs.ids.hackhub.domain.enums.StaffRole;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Retrieves an organizer's hackathon evaluations ordered by score. */
@Service
public class ListHackathonEvaluationsService implements ListHackathonEvaluationsUseCase {
    private final HackathonRepository hackathonRepository;
    private final SubmissionRepository submissionRepository;
    private final EvaluationRepository evaluationRepository;

    public ListHackathonEvaluationsService(
            HackathonRepository hackathonRepository,
            SubmissionRepository submissionRepository,
            EvaluationRepository evaluationRepository
    ) {
        this.hackathonRepository = Objects.requireNonNull(hackathonRepository);
        this.submissionRepository = Objects.requireNonNull(submissionRepository);
        this.evaluationRepository = Objects.requireNonNull(evaluationRepository);
    }

    @Override
    public List<EvaluationResult> list(ListHackathonEvaluationsCommand command) {
        Objects.requireNonNull(command, "command cannot be null");
        var hackathonId = new HackathonId(Objects.requireNonNull(
                command.hackathonId(), "hackathonId cannot be null"));
        var hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(HackathonNotFoundException::new);
        var organizerId = new UserId(Objects.requireNonNull(
                command.organizerId(), "organizerId cannot be null"));
        var isOrganizer = hackathon.staffStructure().assignments().stream()
                .anyMatch(assignment -> assignment.userId().equals(organizerId)
                        && assignment.role() == StaffRole.ORGANIZER);
        if (!isOrganizer) {
            throw new OrganizerNotAuthorizedException();
        }

        if (!hackathon.isEvaluationCompleted()) {
            throw new EvaluationsIncompleteException("The evaluation phase for this hackathon is not yet completed");
        }

        var submissionIds = submissionRepository.findByHackathon(hackathonId).stream()
                .map(Submission::id)
                .toList();

        return evaluationRepository.findAllBySubmissionIds(submissionIds).stream()
                .sorted(Comparator.comparingDouble(Evaluation::score).reversed())
                .map(evaluation -> new EvaluationResult(
                        evaluation.id(),
                        evaluation.submissionId(),
                        evaluation.judgeId().value(),
                        evaluation.score(),
                        evaluation.judgment()))
                .toList();
    }
}
