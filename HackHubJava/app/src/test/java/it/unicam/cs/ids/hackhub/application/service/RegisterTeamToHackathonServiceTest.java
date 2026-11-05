package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.RegisterTeamToHackathonCommand;
import it.unicam.cs.ids.hackhub.application.exception.RegistrationClosedException;
import it.unicam.cs.ids.hackhub.application.exception.TeamAlreadyInActiveHackathonException;
import it.unicam.cs.ids.hackhub.application.exception.TeamAlreadyRegisteredException;
import it.unicam.cs.ids.hackhub.application.exception.TeamMemberIsStaffException;
import it.unicam.cs.ids.hackhub.application.exception.TeamMemberNotAuthorizedException;
import it.unicam.cs.ids.hackhub.domain.entity.Hackathon;
import it.unicam.cs.ids.hackhub.domain.entity.Team;
import it.unicam.cs.ids.hackhub.domain.enums.HackathonState;
import it.unicam.cs.ids.hackhub.domain.enums.StaffRole;
import it.unicam.cs.ids.hackhub.domain.valueobject.PaymentDetails;
import it.unicam.cs.ids.hackhub.domain.valueobject.StaffAssignment;
import it.unicam.cs.ids.hackhub.domain.valueobject.StaffStructure;
import it.unicam.cs.ids.hackhub.domain.valueobject.TimeConstraints;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryHackathonRepository;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryTeamHackathonRegistrationRepository;
import it.unicam.cs.ids.hackhub.infrastructure.persistence.InMemoryTeamRepository;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertThrows;

public class RegisterTeamToHackathonServiceTest {
    @Test
    public void registersTeamWhenAllRulesAreSatisfied() {
        var fixture = new Fixture(3);

        fixture.service.register(new RegisterTeamToHackathonCommand(
                fixture.member.value(), fixture.team.id().value(), fixture.firstHackathon.id().value()));
    }

    @Test
    public void rejectsRegistrationWhenHackathonIsNotOpen() {
        var fixture = new Fixture(3);
        fixture.firstHackathon.changeState(HackathonState.IN_PROGRESS);

        assertThrows(RegistrationClosedException.class, () -> fixture.service.register(
                new RegisterTeamToHackathonCommand(
                        fixture.member.value(), fixture.team.id().value(), fixture.firstHackathon.id().value())));
    }

    @Test
    public void rejectsDuplicateRegistration() {
        var fixture = new Fixture(3);
        var command = new RegisterTeamToHackathonCommand(
                fixture.member.value(), fixture.team.id().value(), fixture.firstHackathon.id().value());
        fixture.service.register(command);

        assertThrows(TeamAlreadyRegisteredException.class, () -> fixture.service.register(command));
    }

    @Test
    public void rejectsAnotherActiveHackathon() {
        var fixture = new Fixture(3);
        fixture.service.register(new RegisterTeamToHackathonCommand(
                fixture.member.value(), fixture.team.id().value(), fixture.firstHackathon.id().value()));

        assertThrows(TeamAlreadyInActiveHackathonException.class, () -> fixture.service.register(
                new RegisterTeamToHackathonCommand(
                        fixture.member.value(), fixture.team.id().value(), fixture.secondHackathon.id().value())));
    }

    @Test
    public void allowsRegistrationAfterPreviousHackathonIsUnderEvaluation() {
        var fixture = new Fixture(3);
        var firstCommand = new RegisterTeamToHackathonCommand(
                fixture.member.value(), fixture.team.id().value(), fixture.firstHackathon.id().value());
        fixture.service.register(firstCommand);
        fixture.firstHackathon.changeState(HackathonState.UNDER_EVALUATION);

        fixture.service.register(new RegisterTeamToHackathonCommand(
                fixture.member.value(), fixture.team.id().value(), fixture.secondHackathon.id().value()));
    }

    @Test
    public void rejectsTeamContainingStaffMember() {
        var userId = new UserId(UUID.randomUUID());
        var team = Team.create("Team", userId, new PaymentDetails("Payment"));
        var fixture = new Fixture(team, 3, List.of(new StaffAssignment(userId, StaffRole.MENTOR)));

        assertThrows(TeamMemberIsStaffException.class, () -> fixture.service.register(
                new RegisterTeamToHackathonCommand(
                        userId.value(), team.id().value(), fixture.firstHackathon.id().value())));
    }

    @Test
    public void rejectsRegistrationByNonMember() {
        var fixture = new Fixture(3);

        assertThrows(TeamMemberNotAuthorizedException.class, () -> fixture.service.register(
                new RegisterTeamToHackathonCommand(
                        UUID.randomUUID(),
                        fixture.team.id().value(),
                        fixture.firstHackathon.id().value())));
    }

    private static class Fixture {
        private final InMemoryTeamRepository teamRepository = new InMemoryTeamRepository();
        private final InMemoryHackathonRepository hackathonRepository = new InMemoryHackathonRepository();
        private final InMemoryTeamHackathonRegistrationRepository registrationRepository =
                new InMemoryTeamHackathonRegistrationRepository();
        private final Team team;
        private final UserId member;
        private final Hackathon firstHackathon;
        private final Hackathon secondHackathon;
        private final RegisterTeamToHackathonService service;

        private Fixture(int maxTeamMembers) {
            this(Team.create(
                    "Team",
                    new UserId(UUID.randomUUID()),
                    new PaymentDetails("Payment")), maxTeamMembers, List.of());
        }

        private Fixture(Team team, int maxTeamMembers, List<StaffAssignment> staff) {
            this.team = team;
            this.member = team.members().get(0);
            this.firstHackathon = createHackathon(maxTeamMembers, staff);
            this.secondHackathon = createHackathon(maxTeamMembers, List.of());
            teamRepository.saveIfMembersAvailable(team);
            hackathonRepository.save(firstHackathon);
            hackathonRepository.save(secondHackathon);
            this.service = new RegisterTeamToHackathonService(
                    teamRepository, hackathonRepository, registrationRepository);
        }

        private Hackathon createHackathon(int maxTeamMembers, List<StaffAssignment> staff) {
            var now = LocalDateTime.now();
            return Hackathon.create(
                    "Hackathon",
                    "Location",
                    BigDecimal.ZERO,
                    maxTeamMembers,
                    new StaffStructure(staff),
                    new TimeConstraints(now.plusDays(1), now.plusDays(2), now.plusDays(3)));
        }
    }
}
