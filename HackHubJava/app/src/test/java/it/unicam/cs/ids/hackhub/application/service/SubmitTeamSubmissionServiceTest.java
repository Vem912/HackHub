package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.SubmitTeamSubmissionCommand;
import it.unicam.cs.ids.hackhub.application.exception.HackathonNotInProgressException;
import it.unicam.cs.ids.hackhub.application.exception.InvalidSubmissionFileException;
import it.unicam.cs.ids.hackhub.application.exception.SubmissionDeadlinePassedException;
import it.unicam.cs.ids.hackhub.application.exception.TeamMemberNotAuthorizedException;
import it.unicam.cs.ids.hackhub.domain.entity.Hackathon;
import it.unicam.cs.ids.hackhub.domain.entity.Team;
import it.unicam.cs.ids.hackhub.domain.enums.HackathonState;
import it.unicam.cs.ids.hackhub.domain.valueobject.PaymentDetails;
import it.unicam.cs.ids.hackhub.domain.valueobject.StaffStructure;
import it.unicam.cs.ids.hackhub.domain.valueobject.SubmissionFile;
import it.unicam.cs.ids.hackhub.domain.valueobject.TimeConstraints;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryHackathonRepository;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemorySubmissionRepository;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryTeamRepository;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SubmitTeamSubmissionServiceTest {
    @Test
    public void createsAndThenReplacesSubmission() {
        var fixture = new Fixture();
        var first = fixture.service.submit(fixture.command("first.zip"));
        var second = fixture.service.submit(fixture.command("second.zip"));

        assertEquals(false, first.updated());
        assertEquals(true, second.updated());
        assertEquals("second.zip", fixture.submissions
                .findByHackathonAndTeam(fixture.hackathon.id(), fixture.team.id()).orElseThrow()
                .file().fileName());
    }

    @Test
    public void rejectsNonMember() {
        var fixture = new Fixture();
        assertThrows(TeamMemberNotAuthorizedException.class, () -> fixture.service.submit(
                new SubmitTeamSubmissionCommand(UUID.randomUUID(), fixture.team.id().value(),
                        fixture.hackathon.id().value(), "Title", "Description", fixture.file("submission.zip"))));
    }

    @Test
    public void rejectsInactiveHackathonAndExpiredDeadline() {
        var fixture = new Fixture();
        fixture.hackathon.changeState(HackathonState.REGISTRATION_OPEN);
        assertThrows(HackathonNotInProgressException.class, () -> fixture.service.submit(fixture.command("submission.zip")));

        fixture.hackathon.changeState(HackathonState.IN_PROGRESS);
        fixture.service = new SubmitTeamSubmissionService(fixture.teams, fixture.hackathons, fixture.submissions,
                new SubmissionFileValidator(), Clock.fixed(Instant.parse("2026-05-04T00:00:00Z"), ZoneOffset.UTC));
        assertThrows(SubmissionDeadlinePassedException.class, () -> fixture.service.submit(fixture.command("submission.zip")));
    }

    @Test
    public void rejectsNonZip() {
        var fixture = new Fixture();
        assertThrows(InvalidSubmissionFileException.class, () -> fixture.service.submit(
                new SubmitTeamSubmissionCommand(fixture.member.value(), fixture.team.id().value(),
                        fixture.hackathon.id().value(), "Title", "Description",
                        new SubmissionFile("submission.txt", new byte[]{1, 2}))));
    }

    private static class Fixture {
        private final InMemoryTeamRepository teams = new InMemoryTeamRepository();
        private final InMemoryHackathonRepository hackathons = new InMemoryHackathonRepository();
        private final InMemorySubmissionRepository submissions = new InMemorySubmissionRepository();
        private final UserId member = new UserId(UUID.randomUUID());
        private final Team team = Team.create("Team", member, new PaymentDetails("Payment"));
        private final Hackathon hackathon;
        private SubmitTeamSubmissionService service;

        private Fixture() {
            var start = LocalDateTime.of(2026, 5, 2, 9, 0);
            hackathon = Hackathon.create("Hackathon", "Location", java.math.BigDecimal.ZERO, 3,
                    new StaffStructure(java.util.List.of()),
                    new TimeConstraints(start.minusDays(1), start, start.plusDays(1)));
            hackathon.changeState(HackathonState.IN_PROGRESS);
            teams.saveIfMembersAvailable(team);
            hackathons.save(hackathon);
            service = new SubmitTeamSubmissionService(teams, hackathons, submissions,
                    new SubmissionFileValidator(), Clock.fixed(
                            start.plusHours(1).toInstant(ZoneOffset.UTC), ZoneOffset.UTC));
        }

        private SubmitTeamSubmissionCommand command(String fileName) {
            return new SubmitTeamSubmissionCommand(member.value(), team.id().value(), hackathon.id().value(),
                    "Title", "Description", file(fileName));
        }

        private SubmissionFile file(String fileName) {
            try {
                var output = new ByteArrayOutputStream();
                try (var zip = new ZipOutputStream(output)) {
                    zip.putNextEntry(new ZipEntry("readme.txt"));
                    zip.write("submission".getBytes());
                    zip.closeEntry();
                }
                return new SubmissionFile(fileName, output.toByteArray());
            } catch (java.io.IOException exception) {
                throw new AssertionError(exception);
            }
        }
    }
}
