package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.RegisterTeamToHackathonCommand;
import it.unicam.cs.ids.hackhub.application.exception.HackathonNotFoundException;
import it.unicam.cs.ids.hackhub.application.exception.RegistrationClosedException;
import it.unicam.cs.ids.hackhub.application.exception.TeamAlreadyInActiveHackathonException;
import it.unicam.cs.ids.hackhub.application.exception.TeamAlreadyRegisteredException;
import it.unicam.cs.ids.hackhub.application.exception.TeamMemberIsStaffException;
import it.unicam.cs.ids.hackhub.application.exception.TeamMemberNotAuthorizedException;
import it.unicam.cs.ids.hackhub.application.exception.TeamNotFoundException;
import it.unicam.cs.ids.hackhub.domain.entity.Hackathon;
import it.unicam.cs.ids.hackhub.domain.entity.Team;
import it.unicam.cs.ids.hackhub.domain.enums.HackathonState;
import it.unicam.cs.ids.hackhub.domain.enums.StaffRole;
import it.unicam.cs.ids.hackhub.domain.valueobject.StaffAssignment;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryHackathonRepository;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryTeamHackathonRegistrationRepository;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryTeamRepository;
import it.unicam.cs.ids.hackhub.support.TestDataFactory;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertThrows;

public class RegisterTeamToHackathonServiceTest {
    @Test
    public void registersTeamWhenAllRulesAreSatisfied() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var hackathon = TestDataFactory.hackathon(3, HackathonState.REGISTRATION_OPEN, List.of());
        var service = service(team, hackathon);

        service.register(command(member, team, hackathon));
    }

    @Test
    public void rejectsRegistrationWhenHackathonIsNotOpen() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var hackathon = TestDataFactory.hackathon(3, HackathonState.IN_PROGRESS, List.of());

        assertThrows(RegistrationClosedException.class,
                () -> service(team, hackathon).register(command(member, team, hackathon)));
    }

    @Test
    public void rejectsDuplicateRegistration() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var hackathon = TestDataFactory.hackathon(3, HackathonState.REGISTRATION_OPEN, List.of());
        var service = service(team, hackathon);
        var command = command(member, team, hackathon);

        service.register(command);

        assertThrows(TeamAlreadyRegisteredException.class, () -> service.register(command));
    }

    @Test
    public void rejectsAnotherActiveHackathon() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var firstHackathon = TestDataFactory.hackathon(3, HackathonState.REGISTRATION_OPEN, List.of());
        var secondHackathon = TestDataFactory.hackathon(3, HackathonState.REGISTRATION_OPEN, List.of());
        var service = service(team, firstHackathon, secondHackathon);

        service.register(command(member, team, firstHackathon));

        assertThrows(TeamAlreadyInActiveHackathonException.class,
                () -> service.register(command(member, team, secondHackathon)));
    }

    @Test
    public void allowsRegistrationAfterPreviousHackathonIsUnderEvaluation() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var firstHackathon = TestDataFactory.hackathon(3, HackathonState.REGISTRATION_OPEN, List.of());
        var secondHackathon = TestDataFactory.hackathon(3, HackathonState.REGISTRATION_OPEN, List.of());
        var service = service(team, firstHackathon, secondHackathon);

        service.register(command(member, team, firstHackathon));
        firstHackathon.changeState(HackathonState.UNDER_EVALUATION);

        service.register(command(member, team, secondHackathon));
    }

    @Test
    public void rejectsTeamContainingStaffMember() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var hackathon = TestDataFactory.hackathon(
                3,
                HackathonState.REGISTRATION_OPEN,
                List.of(new StaffAssignment(member, StaffRole.MENTOR)));

        assertThrows(TeamMemberIsStaffException.class,
                () -> service(team, hackathon).register(command(member, team, hackathon)));
    }

    @Test
    public void rejectsRegistrationByNonMember() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var hackathon = TestDataFactory.hackathon(3, HackathonState.REGISTRATION_OPEN, List.of());

        assertThrows(TeamMemberNotAuthorizedException.class,
                () -> service(team, hackathon).register(command(TestDataFactory.userId(), team, hackathon)));
    }

    @Test
    public void rejectsMissingTeam() {
        var member = TestDataFactory.userId();
        var hackathon = TestDataFactory.hackathon(3, HackathonState.REGISTRATION_OPEN, List.of());
        var team = TestDataFactory.team(member);

        assertThrows(TeamNotFoundException.class,
                () -> service(null, hackathon).register(command(member, team, hackathon)));
    }

    @Test
    public void rejectsMissingHackathon() {
        var member = TestDataFactory.userId();
        var team = TestDataFactory.team(member);
        var hackathon = TestDataFactory.hackathon(3, HackathonState.REGISTRATION_OPEN, List.of());
        var service = service(team, (Hackathon) null);

        assertThrows(HackathonNotFoundException.class,
                () -> service.register(command(member, team, hackathon)));
    }

    private static RegisterTeamToHackathonCommand command(
            UserId userId,
            Team team,
            Hackathon hackathon) {
        return new RegisterTeamToHackathonCommand(userId.value(), team.id().value(), hackathon.id().value());
    }

    private static RegisterTeamToHackathonService service(Team team, Hackathon... hackathons) {
        var teams = new InMemoryTeamRepository();
        var repository = new InMemoryHackathonRepository();
        var registrations = new InMemoryTeamHackathonRegistrationRepository();
        if (team != null) {
            teams.saveIfMembersAvailable(team);
        }
        for (var hackathon : hackathons) {
            if (hackathon != null) {
                repository.save(hackathon);
            }
        }
        return new RegisterTeamToHackathonService(teams, repository, registrations);
    }
}
