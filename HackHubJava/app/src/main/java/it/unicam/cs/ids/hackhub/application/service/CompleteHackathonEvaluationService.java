package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.CompleteHackathonEvaluationCommand;
import it.unicam.cs.ids.hackhub.application.exception.EvaluationAlreadyCompletedException;
import it.unicam.cs.ids.hackhub.application.exception.EvaluationsIncompleteException;
import it.unicam.cs.ids.hackhub.application.exception.HackathonNotFoundException;
import it.unicam.cs.ids.hackhub.application.exception.JudgeNotAuthorizedException;
import it.unicam.cs.ids.hackhub.application.port.in.CompleteHackathonEvaluationUseCase;
import it.unicam.cs.ids.hackhub.application.port.out.EvaluationRepository;
import it.unicam.cs.ids.hackhub.application.port.out.HackathonRepository;
import it.unicam.cs.ids.hackhub.application.port.out.SubmissionRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Evaluation;
import it.unicam.cs.ids.hackhub.domain.entity.Submission;
import it.unicam.cs.ids.hackhub.domain.enums.StaffRole;
import it.unicam.cs.ids.hackhub.domain.valueobject.HackathonId;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

/** Completes an hackathon evaluation phase after every submission was evaluated. */
@Service
public class CompleteHackathonEvaluationService implements CompleteHackathonEvaluationUseCase {
    private final HackathonRepository hackathonRepository;
    private final SubmissionRepository submissionRepository;
    private final EvaluationRepository evaluationRepository;

    public CompleteHackathonEvaluationService(
            HackathonRepository hackathonRepository,
            SubmissionRepository submissionRepository,
            EvaluationRepository evaluationRepository
    ) {
        this.hackathonRepository = Objects.requireNonNull(hackathonRepository);
        this.submissionRepository = Objects.requireNonNull(submissionRepository);
        this.evaluationRepository = Objects.requireNonNull(evaluationRepository);
    }

    /**
     * Marks the evaluation phase as completed when every submission has an evaluation.
     *
     * @throws HackathonNotFoundException     if the hackathon does not exist
     * @throws EvaluationsIncompleteException if at least one submission is not evaluated
     */
    @Override
    public void completeEvaluation(CompleteHackathonEvaluationCommand command) {
        Objects.requireNonNull(command, "command cannot be null");

        var hackathon = hackathonRepository.findById(
                        new HackathonId(Objects.requireNonNull(
                                command.hackathonId(), "hackathonId cannot be null")))
                .orElseThrow(HackathonNotFoundException::new);
        var judgeId = new UserId(
                Objects.requireNonNull(command.judgeId(), "judgeId cannot be null"));
        var isJudge = hackathon.staffStructure().assignments().stream()
                .anyMatch(assignment -> assignment.userId().equals(judgeId)
                        && assignment.role() == StaffRole.JUDGE);
        if (!isJudge) {
            throw new JudgeNotAuthorizedException();
        }
        if (hackathon.isEvaluationCompleted()) {
            throw new EvaluationAlreadyCompletedException();
        }

        var submissionIds = submissionRepository.findByHackathon(hackathon.id()).stream()
                .map(Submission::id)
                .collect(Collectors.toSet());
        var evaluatedSubmissionIds = evaluationRepository.findAllBySubmissionIds(submissionIds).stream()
                .map(Evaluation::submissionId)
                .collect(Collectors.toSet());

        if (!evaluatedSubmissionIds.containsAll(submissionIds)) {
            throw new EvaluationsIncompleteException("The evaluation phase cannot be completed because some submissions have not been evaluated.");
        }

        hackathon.completeEvaluation();
        hackathonRepository.save(hackathon);
    }
}
