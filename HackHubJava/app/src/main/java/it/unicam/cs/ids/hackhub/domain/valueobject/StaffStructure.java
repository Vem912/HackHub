package it.unicam.cs.ids.hackhub.domain.valueobject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Immutable staff assignments for one hackathon.
 * A user may appear only once and therefore hold at most one staff role.
 */
public record StaffStructure(List<StaffAssignment> assignments) {
    public StaffStructure {
        assignments = List.copyOf(assignments);

        Set<UserId> assignedUsers = new HashSet<>();
        for (StaffAssignment assignment : assignments) {
            if (!assignedUsers.add(assignment.userId())) {
                throw new IllegalArgumentException(
                        "A user cannot have more than one staff role in the same hackathon: " + assignment.userId());
            }
        }
    }
}
