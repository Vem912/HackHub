package it.unicam.cs.ids.hackhub.support;

import it.unicam.cs.ids.hackhub.domain.entity.Hackathon;
import it.unicam.cs.ids.hackhub.domain.entity.Team;
import it.unicam.cs.ids.hackhub.domain.enums.HackathonState;
import it.unicam.cs.ids.hackhub.domain.valueobject.PaymentDetails;
import it.unicam.cs.ids.hackhub.domain.valueobject.StaffAssignment;
import it.unicam.cs.ids.hackhub.domain.valueobject.StaffStructure;
import it.unicam.cs.ids.hackhub.domain.valueobject.SubmissionFile;
import it.unicam.cs.ids.hackhub.domain.valueobject.TimeConstraints;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class TestDataFactory {
    public static final LocalDateTime HACKATHON_START = LocalDateTime.of(2026, 5, 2, 9, 0);

    private TestDataFactory() {
    }

    public static UserId userId() {
        return new UserId(UUID.randomUUID());
    }

    public static Team team(UserId member) {
        return Team.create("Team", member, new PaymentDetails("Payment"));
    }

    public static Hackathon hackathon(
            int maxTeamMembers,
            HackathonState state,
            List<StaffAssignment> staff) {
        var hackathon = Hackathon.create(
                "Hackathon",
                "Location",
                java.math.BigDecimal.ZERO,
                maxTeamMembers,
                new StaffStructure(staff),
                new TimeConstraints(
                        HACKATHON_START.minusDays(1),
                        HACKATHON_START,
                        HACKATHON_START.plusDays(1)));
        hackathon.changeState(state);
        return hackathon;
    }

    public static SubmissionFile zipFile(String fileName) {
        try {
            var output = new ByteArrayOutputStream();
            try (var zip = new ZipOutputStream(output)) {
                zip.putNextEntry(new ZipEntry("readme.txt"));
                zip.write("submission".getBytes());
                zip.closeEntry();
            }
            return new SubmissionFile(fileName, output.toByteArray());
        } catch (IOException exception) {
            throw new AssertionError("Could not create test ZIP file", exception);
        }
    }

    public static Clock fixedClock(LocalDateTime dateTime) {
        return Clock.fixed(dateTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    }
}
