package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.CompleteHackathonEvaluationCommand;
import it.unicam.cs.ids.hackhub.application.exception.EvaluationAlreadyCompletedException;
import it.unicam.cs.ids.hackhub.application.exception.EvaluationsIncompleteException;
import it.unicam.cs.ids.hackhub.application.exception.JudgeNotAuthorizedException;
import it.unicam.cs.ids.hackhub.domain.entity.Evaluation;
import it.unicam.cs.ids.hackhub.domain.entity.Submission;
import it.unicam.cs.ids.hackhub.domain.enums.HackathonState;
import it.unicam.cs.ids.hackhub.domain.enums.StaffRole;
import it.unicam.cs.ids.hackhub.domain.valueobject.StaffAssignment;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryEvaluationRepository;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryHackathonRepository;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemorySubmissionRepository;
import it.unicam.cs.ids.hackhub.support.TestDataFactory;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

public class CompleteHackathonEvaluationServiceTest {
    @Test
    public void completesWhenEverySubmissionHasAnEvaluation() {
        var judge = TestDataFactory.userId();
        var hackathon = TestDataFactory.hackathon(
                3, HackathonState.UNDER_EVALUATION,
                List.of(new StaffAssignment(judge, StaffRole.JUDGE)));
        var submission = submission(hackathon);
        var submissions = new InMemorySubmissionRepository();
        submissions.save(submission);
        var evaluations = new InMemoryEvaluationRepository();
        evaluations.save(Evaluation.create(submission.id(), judge, BigDecimal.valueOf(8), "Good solution."));
        var hackathons = repository(hackathon);

        service(hackathons, submissions, evaluations).completeEvaluation(
                new CompleteHackathonEvaluationCommand(hackathon.id().value(), judge.value()));

        assertTrue(hackathon.isEvaluationCompleted());
        assertTrue(hackathons.findById(hackathon.id()).orElseThrow().isEvaluationCompleted());
    }

    @Test
    public void rejectsWhenAtLeastOneSubmissionIsNotEvaluated() {
        var judge = TestDataFactory.userId();
        var hackathon = TestDataFactory.hackathon(
                3, HackathonState.UNDER_EVALUATION,
                List.of(new StaffAssignment(judge, StaffRole.JUDGE)));
        var submissions = new InMemorySubmissionRepository();
        submissions.save(submission(hackathon));
        var another = submission(hackathon);
        submissions.save(another);

        assertThrows(EvaluationsIncompleteException.class,
                () -> service(repository(hackathon), submissions,
                        new InMemoryEvaluationRepository()).completeEvaluation(
                        new CompleteHackathonEvaluationCommand(
                                hackathon.id().value(), judge.value())));
        assertTrue(!hackathon.isEvaluationCompleted());
    }

    @Test
    public void rejectsUnauthorizedJudge() {
        var judge = TestDataFactory.userId();
        var hackathon = TestDataFactory.hackathon(
                3, HackathonState.UNDER_EVALUATION,
                List.of(new StaffAssignment(judge, StaffRole.JUDGE)));

        assertThrows(JudgeNotAuthorizedException.class,
                () -> service(repository(hackathon),
                        new InMemorySubmissionRepository(),
                        new InMemoryEvaluationRepository()).completeEvaluation(
                        new CompleteHackathonEvaluationCommand(
                                hackathon.id().value(), TestDataFactory.userId().value())));
    }

    @Test
    public void rejectsRepeatedCompletion() {
        var judge = TestDataFactory.userId();
        var hackathon = TestDataFactory.hackathon(
                3, HackathonState.UNDER_EVALUATION,
                List.of(new StaffAssignment(judge, StaffRole.JUDGE)));
        hackathon.completeEvaluation();

        assertThrows(EvaluationAlreadyCompletedException.class,
                () -> service(repository(hackathon),
                        new InMemorySubmissionRepository(),
                        new InMemoryEvaluationRepository()).completeEvaluation(
                        new CompleteHackathonEvaluationCommand(
                                hackathon.id().value(), judge.value())));
    }

    private static Submission submission(it.unicam.cs.ids.hackhub.domain.entity.Hackathon hackathon) {
        return Submission.create(
                hackathon.id(),
                TestDataFactory.team(TestDataFactory.userId()).id(),
                "Title",
                "Description",
                TestDataFactory.zipFile("submission.zip"),
                TestDataFactory.HACKATHON_START);
    }

    private static CompleteHackathonEvaluationService service(
            InMemoryHackathonRepository hackathons,
            InMemorySubmissionRepository submissions,
            InMemoryEvaluationRepository evaluations) {
        return new CompleteHackathonEvaluationService(hackathons, submissions, evaluations);
    }

    private static InMemoryHackathonRepository repository(
            it.unicam.cs.ids.hackhub.domain.entity.Hackathon hackathon) {
        var repository = new InMemoryHackathonRepository();
        repository.save(hackathon);
        return repository;
    }
}
