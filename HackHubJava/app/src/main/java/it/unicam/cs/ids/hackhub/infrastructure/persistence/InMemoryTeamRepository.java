package it.unicam.cs.ids.hackhub.infrastructure.persistence;

import it.unicam.cs.ids.hackhub.application.port.out.TeamRepository;
import it.unicam.cs.ids.hackhub.domain.entity.Team;
import it.unicam.cs.ids.hackhub.domain.valueobject.TeamId;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * In-memory adapter for the team persistence port.
 *
 * <p>The second index tracks team membership so that a user cannot be saved
 * in more than one team. This implementation is intended for development and
 * tests: all data is lost when the application stops.</p>
 */
@Repository
public class InMemoryTeamRepository implements TeamRepository {
    private final Map<UUID, Team> teams = new HashMap<>();
    private final Map<UserId, UUID> teamsByMember = new HashMap<>();

    /**
     * Saves a team only when none of its members already belongs to a team.
     *
     * @return {@code true} when the team was saved, otherwise {@code false}
     */
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

    /** Finds a team by identifier, returning an empty result when it is absent. */
    @Override
    public synchronized Optional<Team> findById(TeamId teamId) {
        return Optional.ofNullable(teams.get(teamId.value()));
    }
}
