package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.ListHackathonEvaluationsCommand;
import it.unicam.cs.ids.hackhub.application.exception.OrganizerNotAuthorizedException;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ListHackathonEvaluationsServiceTest {
    @Test
    public void returnsEvaluationsOrderedByDescendingScore() {
        var organizer = TestDataFactory.userId();
        var judge = TestDataFactory.userId();
        var hackathon = TestDataFactory.hackathon(
                3, HackathonState.UNDER_EVALUATION,
                List.of(new StaffAssignment(organizer, StaffRole.ORGANIZER)));
        hackathon.completeEvaluation();
        var first = submission(hackathon);
        var second = submission(hackathon);
        var submissions = new InMemorySubmissionRepository();
        submissions.save(first);
        submissions.save(second);
        var evaluations = new InMemoryEvaluationRepository();
        evaluations.save(Evaluation.create(first.id(), judge, BigDecimal.valueOf(4.5), "Okay."));
        evaluations.save(Evaluation.create(second.id(), judge, BigDecimal.valueOf(9.5), "Excellent."));

        var result = service(hackathon, submissions, evaluations).list(
                new ListHackathonEvaluationsCommand(
                        hackathon.id().value(), organizer.value()));

        assertEquals(2, result.size());
        assertEquals(second.id(), result.get(0).submissionId());
        assertEquals(BigDecimal.valueOf(9.5), result.get(0).score());
        assertEquals(first.id(), result.get(1).submissionId());
    }

    @Test
    public void rejectsNonOrganizer() {
        var organizer = TestDataFactory.userId();
        var hackathon = TestDataFactory.hackathon(
                3, HackathonState.UNDER_EVALUATION,
                List.of(new StaffAssignment(organizer, StaffRole.ORGANIZER)));

        assertThrows(OrganizerNotAuthorizedException.class,
                () -> service(hackathon, new InMemorySubmissionRepository(),
                        new InMemoryEvaluationRepository()).list(
                        new ListHackathonEvaluationsCommand(
                                hackathon.id().value(), TestDataFactory.userId().value())));
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

    private static ListHackathonEvaluationsService service(
            it.unicam.cs.ids.hackhub.domain.entity.Hackathon hackathon,
            InMemorySubmissionRepository submissions,
            InMemoryEvaluationRepository evaluations) {
        var hackathons = new InMemoryHackathonRepository();
        hackathons.save(hackathon);
        return new ListHackathonEvaluationsService(hackathons, submissions, evaluations);
    }
}
