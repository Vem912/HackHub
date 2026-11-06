package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.EvaluateSubmissionCommand;
import it.unicam.cs.ids.hackhub.application.exception.JudgeNotAuthorizedException;
import it.unicam.cs.ids.hackhub.application.port.out.EvaluationRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Submission;
import it.unicam.cs.ids.hackhub.domain.enums.HackathonState;
import it.unicam.cs.ids.hackhub.domain.enums.StaffRole;
import it.unicam.cs.ids.hackhub.domain.valueobject.StaffAssignment;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryEvaluationRepository;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryHackathonRepository;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemorySubmissionRepository;
import it.unicam.cs.ids.hackhub.support.TestDataFactory;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class EvaluateSubmissionServiceTest {
    @Test
    public void recordsEvaluationAndUpdatesEvaluatedCounter() {
        var judge = TestDataFactory.userId();
        var teamMember = TestDataFactory.userId();
        var hackathon = TestDataFactory.hackathon(
                3, HackathonState.IN_PROGRESS,
                List.of(new StaffAssignment(judge, StaffRole.JUDGE)));
        var team = TestDataFactory.team(teamMember);
        var submission = Submission.create(
                hackathon.id(), team.id(), "Title", "Description",
                TestDataFactory.zipFile("submission.zip"), TestDataFactory.HACKATHON_START);
        var submissions = new InMemorySubmissionRepository();
        submissions.save(submission);
        var service = service(hackathon, submissions, new InMemoryEvaluationRepository());

        var result = service.evaluate(new EvaluateSubmissionCommand(
                submission.id(), judge.value(), 8, "Good solution."));

        assertEquals(8, result.score());
        assertEquals(1, result.evaluatedSubmissionsCount());
    }

    @Test
    public void rejectsUserThatIsNotAJudge() {
        var judge = TestDataFactory.userId();
        var hackathon = TestDataFactory.hackathon(
                3, HackathonState.IN_PROGRESS,
                List.of(new StaffAssignment(judge, StaffRole.JUDGE)));
        var team = TestDataFactory.team(TestDataFactory.userId());
        var submission = Submission.create(
                hackathon.id(), team.id(), "Title", "Description",
                TestDataFactory.zipFile("submission.zip"), TestDataFactory.HACKATHON_START);
        var submissions = new InMemorySubmissionRepository();
        submissions.save(submission);

        assertThrows(JudgeNotAuthorizedException.class,
                () -> service(hackathon, submissions, new InMemoryEvaluationRepository())
                        .evaluate(new EvaluateSubmissionCommand(
                                submission.id(), TestDataFactory.userId().value(), 8, "Good solution.")));
    }

    @Test
    public void returnsAllHackathonSubmissionsWithFileContent() {
        var hackathon = TestDataFactory.hackathon(3, HackathonState.IN_PROGRESS, List.of());
        var team = TestDataFactory.team(TestDataFactory.userId());
        var submission = Submission.create(
                hackathon.id(), team.id(), "Title", "Description",
                TestDataFactory.zipFile("submission.zip"), TestDataFactory.HACKATHON_START);
        var submissions = new InMemorySubmissionRepository();
        submissions.save(submission);

        var result = new ListHackathonSubmissionsService(
                repository(hackathon), submissions).list(hackathon.id().value());

        assertEquals(1, result.size());
        assertEquals("submission.zip", result.get(0).fileName());
        assertEquals(submission.file().content().length, result.get(0).fileContent().length);
    }

    private static EvaluateSubmissionService service(
            it.unicam.cs.ids.hackhub.domain.entity.Hackathon hackathon,
            InMemorySubmissionRepository submissions,
            EvaluationRepository evaluations) {
        return new EvaluateSubmissionService(submissions, repository(hackathon), evaluations);
    }

    private static InMemoryHackathonRepository repository(
            it.unicam.cs.ids.hackhub.domain.entity.Hackathon hackathon) {
        var repository = new InMemoryHackathonRepository();
        repository.save(hackathon);
        return repository;
    }
}
