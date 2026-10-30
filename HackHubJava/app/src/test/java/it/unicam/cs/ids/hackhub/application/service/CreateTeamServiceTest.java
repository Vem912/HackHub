package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.CreateTeamCommand;
import it.unicam.cs.ids.hackhub.application.exception.UserAlreadyInTeamException;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryTeamRepository;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class CreateTeamServiceTest {
    @Test
    public void createsTeamForUserWithoutExistingTeam() {
        var service = new CreateTeamService(new InMemoryTeamRepository());
        var command = new CreateTeamCommand(
                "HackHub",
                UUID.randomUUID(),
                "Payment details");

        var result = service.create(command);

        assertEquals("HackHub", result.name());
        assertNotNull(result.id());
    }

    @Test
    public void rejectsUserWhoAlreadyBelongsToATeam() {
        var service = new CreateTeamService(new InMemoryTeamRepository());
        UUID userId = UUID.randomUUID();
        service.create(new CreateTeamCommand("First", userId, "Payment details"));

        assertThrows(
                UserAlreadyInTeamException.class,
                () -> service.create(new CreateTeamCommand("Second", userId, "Payment details")));
    }

    @Test
    public void allowsDifferentUsersToCreateTeams() {
        var service = new CreateTeamService(new InMemoryTeamRepository());

        var first = service.create(new CreateTeamCommand(
                "First", UUID.randomUUID(), "First payment details"));
        var second = service.create(new CreateTeamCommand(
                "Second", UUID.randomUUID(), "Second payment details"));

        assertNotEquals(first.id(), second.id());
    }
}
