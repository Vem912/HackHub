package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.EvaluateSubmissionCommand;
import it.unicam.cs.ids.hackhub.application.dto.result.EvaluateSubmissionResult;
import it.unicam.cs.ids.hackhub.application.exception.HackathonNotFoundException;
import it.unicam.cs.ids.hackhub.application.exception.JudgeNotAuthorizedException;
import it.unicam.cs.ids.hackhub.application.exception.SubmissionNotFoundException;
import it.unicam.cs.ids.hackhub.application.port.in.EvaluateSubmissionUseCase;
import it.unicam.cs.ids.hackhub.application.port.out.EvaluationRepository;
import it.unicam.cs.ids.hackhub.application.port.out.HackathonRepository;
import it.unicam.cs.ids.hackhub.application.port.out.SubmissionRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Evaluation;
import it.unicam.cs.ids.hackhub.domain.enums.StaffRole;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import org.springframework.stereotype.Service;

import java.util.Objects;

/** Validates and records a judge's evaluation of a submission. */
@Service
public class EvaluateSubmissionService implements EvaluateSubmissionUseCase {
    private final SubmissionRepository submissionRepository;
    private final HackathonRepository hackathonRepository;
    private final EvaluationRepository evaluationRepository;

    public EvaluateSubmissionService(
            SubmissionRepository submissionRepository,
            HackathonRepository hackathonRepository,
            EvaluationRepository evaluationRepository
    ) {
        this.submissionRepository = Objects.requireNonNull(submissionRepository);
        this.hackathonRepository = Objects.requireNonNull(hackathonRepository);
        this.evaluationRepository = Objects.requireNonNull(evaluationRepository);
    }

    @Override
    public EvaluateSubmissionResult evaluate(EvaluateSubmissionCommand command) {
        Objects.requireNonNull(command, "command cannot be null");
        var submission = submissionRepository.findById(
                        Objects.requireNonNull(command.submissionId(), "submissionId cannot be null"))
                .orElseThrow(SubmissionNotFoundException::new);
        var hackathon = hackathonRepository.findById(submission.hackathonId())
                .orElseThrow(HackathonNotFoundException::new);
        var judgeId = new UserId(Objects.requireNonNull(command.judgeId(), "judgeId cannot be null"));
        var isJudge = hackathon.staffStructure().assignments().stream()
                .anyMatch(assignment -> assignment.userId().equals(judgeId)
                        && assignment.role() == StaffRole.JUDGE);
        if (!isJudge) {
            throw new JudgeNotAuthorizedException();
        }
        var evaluation = Evaluation.create(
                submission.id(), judgeId, command.score(), command.judgment());
        evaluationRepository.save(evaluation);
        var evaluatedSubmissionsCount = evaluationRepository.countBySubmissionIds(
                submissionRepository.findByHackathon(submission.hackathonId()).stream()
                        .map(it.unicam.cs.ids.hackhub.domain.entity.Submission::id)
                        .toList());
        return new EvaluateSubmissionResult(
                evaluation.id(), evaluation.submissionId(), evaluation.judgeId().value(),
                evaluation.score(), evaluation.judgment(), evaluatedSubmissionsCount);
    }

}
