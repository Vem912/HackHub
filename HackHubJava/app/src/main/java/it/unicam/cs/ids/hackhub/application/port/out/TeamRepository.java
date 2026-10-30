package it.unicam.cs.ids.hackhub.application.port.out;

import it.unicam.cs.ids.hackhub.domain.entity.Team;

/** Outbound persistence port used by the application to store teams. */
public interface TeamRepository {
    /**
     * Stores the team only if none of its members already belongs to a team.
     *
     * @return true when stored, false when at least one member already belongs to a team
     */
    boolean saveIfMembersAvailable(Team team);
}
