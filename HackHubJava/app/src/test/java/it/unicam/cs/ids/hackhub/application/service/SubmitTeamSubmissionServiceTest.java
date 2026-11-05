package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.SubmitTeamSubmissionCommand;
import it.unicam.cs.ids.hackhub.application.exception.HackathonNotFoundException;
import it.unicam.cs.ids.hackhub.application.exception.HackathonNotInProgressException;
import it.unicam.cs.ids.hackhub.application.exception.InvalidSubmissionFileException;
import it.unicam.cs.ids.hackhub.application.exception.SubmissionDeadlinePassedException;
import it.unicam.cs.ids.hackhub.application.exception.TeamMemberNotAuthorizedException;
import it.unicam.cs.ids.hackhub.application.exception.TeamNotFoundException;
import it.unicam.cs.ids.hackhub.domain.entity.Hackathon;
import it.unicam.cs.ids.hackhub.domain.entity.Team;
import it.unicam.cs.ids.hackhub.domain.enums.HackathonState;
import it.unicam.cs.ids.hackhub.domain.valueobject.PaymentDetails;
import it.unicam.cs.ids.hackhub.domain.valueobject.SubmissionFile;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryHackathonRepository;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemorySubmissionRepository;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryTeamRepository;
import it.unicam.cs.ids.hackhub.support.TestDataFactory;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SubmitTeamSubmissionServiceTest {
    @Test
    public void createsAndThenReplacesSubmission() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var hackathon = TestDataFactory.hackathon(3, HackathonState.IN_PROGRESS, List.of());
        var submissions = new InMemorySubmissionRepository();
        var service = service(team, hackathon, submissions, TestDataFactory.HACKATHON_START.plusHours(1));

        var first = service.submit(command(member, team, hackathon, "first.zip"));
        var second = service.submit(command(member, team, hackathon, "second.zip"));

        assertTrue(second.updated());
        assertTrue(!first.updated());
        assertTrue(submissions.findByHackathonAndTeam(hackathon.id(), team.id()).orElseThrow()
                .file().fileName().equals("second.zip"));
    }

    @Test
    public void rejectsNonMember() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var hackathon = TestDataFactory.hackathon(3, HackathonState.IN_PROGRESS, List.of());

        assertThrows(TeamMemberNotAuthorizedException.class,
                () -> service(team, hackathon, new InMemorySubmissionRepository(),
                        TestDataFactory.HACKATHON_START.plusHours(1))
                        .submit(command(TestDataFactory.userId(), team, hackathon, "submission.zip")));
    }

    @Test
    public void rejectsInactiveHackathon() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var hackathon = TestDataFactory.hackathon(3, HackathonState.REGISTRATION_OPEN, List.of());

        assertThrows(HackathonNotInProgressException.class,
                () -> service(team, hackathon, new InMemorySubmissionRepository(),
                        TestDataFactory.HACKATHON_START.plusHours(1))
                        .submit(command(member, team, hackathon, "submission.zip")));
    }

    @Test
    public void rejectsExpiredDeadline() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var hackathon = TestDataFactory.hackathon(3, HackathonState.IN_PROGRESS, List.of());

        assertThrows(SubmissionDeadlinePassedException.class,
                () -> service(team, hackathon, new InMemorySubmissionRepository(),
                        TestDataFactory.HACKATHON_START.plusDays(1).plusMinutes(1))
                        .submit(command(member, team, hackathon, "submission.zip")));
    }

    @Test
    public void rejectsNonZip() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var hackathon = TestDataFactory.hackathon(3, HackathonState.IN_PROGRESS, List.of());
        var nonZip = new SubmissionFile("submission.txt", new byte[]{1, 2});

        assertThrows(InvalidSubmissionFileException.class,
                () -> service(team, hackathon, new InMemorySubmissionRepository(),
                        TestDataFactory.HACKATHON_START.plusHours(1))
                        .submit(command(member, team, hackathon, nonZip)));
    }

    @Test
    public void rejectsMissingTeam() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var hackathon = TestDataFactory.hackathon(3, HackathonState.IN_PROGRESS, List.of());

        assertThrows(TeamNotFoundException.class,
                () -> service(null, hackathon, new InMemorySubmissionRepository(),
                        TestDataFactory.HACKATHON_START.plusHours(1))
                        .submit(command(member, team, hackathon, "submission.zip")));
    }

    @Test
    public void rejectsMissingHackathon() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var hackathon = TestDataFactory.hackathon(3, HackathonState.IN_PROGRESS, List.of());

        assertThrows(HackathonNotFoundException.class,
                () -> service(team, null, new InMemorySubmissionRepository(),
                        TestDataFactory.HACKATHON_START.plusHours(1))
                        .submit(command(member, team, hackathon, "submission.zip")));
    }

    private static SubmitTeamSubmissionCommand command(
            UserId userId,
            Team team,
            Hackathon hackathon,
            String fileName) {
        return command(userId, team, hackathon, TestDataFactory.zipFile(fileName));
    }

    private static SubmitTeamSubmissionCommand command(
            UserId userId,
            Team team,
            Hackathon hackathon,
            SubmissionFile file) {
        return new SubmitTeamSubmissionCommand(
                userId.value(),
                team.id().value(),
                hackathon.id().value(),
                "Title",
                "Description",
                file);
    }

    private static SubmitTeamSubmissionService service(
            Team team,
            Hackathon hackathon,
            InMemorySubmissionRepository submissions,
            java.time.LocalDateTime now) {
        var teams = new InMemoryTeamRepository();
        var hackathons = new InMemoryHackathonRepository();
        if (team != null) {
            teams.saveIfMembersAvailable(team);
        }
        if (hackathon != null) {
            hackathons.save(hackathon);
        }
        return new SubmitTeamSubmissionService(
                teams,
                hackathons,
                submissions,
                new SubmissionFileValidator(),
                TestDataFactory.fixedClock(now));
    }
}
