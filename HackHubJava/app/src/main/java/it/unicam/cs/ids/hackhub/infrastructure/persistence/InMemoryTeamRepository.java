package it.unicam.cs.ids.hackhub.infrastructure.persistence;

import it.unicam.cs.ids.hackhub.application.port.out.TeamRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Team;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** In-memory repository adapter; stored data is lost when the application stops. */
@Repository
public class InMemoryTeamRepository implements TeamRepository {
    private final Map<UUID, Team> teams = new HashMap<>();
    private final Map<UserId, UUID> teamsByMember = new HashMap<>();

    @Override
    public synchronized boolean saveIfMembersAvailable(Team team) {
        if (team.members().stream().anyMatch(teamsByMember::containsKey)) {
            return false;
        }

        UUID teamId = team.id().value();
        teams.put(teamId, team);
        team.members().forEach(memberId -> teamsByMember.put(memberId, teamId));
        return true;
    }
}
