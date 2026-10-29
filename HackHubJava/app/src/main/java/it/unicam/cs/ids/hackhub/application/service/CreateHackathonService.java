package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.dto.command.CreateHackathonCommand;
import it.unicam.cs.ids.hackhub.application.dto.result.CreateHackathonResult;
import it.unicam.cs.ids.hackhub.application.port.in.CreateHackathonUseCase;
import it.unicam.cs.ids.hackhub.application.port.out.HackathonRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Hackathon;
import it.unicam.cs.ids.hackhub.domain.enums.StaffRole;
import it.unicam.cs.ids.hackhub.domain.valueobject.StaffAssignment;
import it.unicam.cs.ids.hackhub.domain.valueobject.StaffStructure;
import it.unicam.cs.ids.hackhub.domain.valueobject.TimeConstraints;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** Implements the create-hackathon use case by building the domain model and persisting it. */
@Service
public class CreateHackathonService implements CreateHackathonUseCase {
    private final HackathonRepository hackathonRepository;

    public CreateHackathonService(HackathonRepository hackathonRepository) {
        this.hackathonRepository = hackathonRepository;
    }

    @Override
    public CreateHackathonResult create(CreateHackathonCommand command) {
        var staff = new ArrayList<StaffAssignment>();
        staff.add(new StaffAssignment(new UserId(command.organizerId()), StaffRole.ORGANIZER));
        staff.add(new StaffAssignment(new UserId(command.judgeId()), StaffRole.JUDGE));
        command.mentorIds().stream()
                .map(userId -> new StaffAssignment(new UserId(userId), StaffRole.MENTOR))
                .forEach(staff::add);

        var hackathon = Hackathon.create(
                command.name(),
                command.location(),
                command.prize(),
                new StaffStructure(List.copyOf(staff)),
                new TimeConstraints(
                        command.teamRegistrationDeadline(),
                        command.hackathonStart(),
                        command.hackathonEnd()));

        hackathonRepository.save(hackathon);
        return toResult(hackathon);
    }

    private CreateHackathonResult toResult(Hackathon hackathon) {
        return new CreateHackathonResult(hackathon.id().value(), hackathon.name());
    }
}
