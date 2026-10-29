package it.unicam.cs.ids.hackhub.domain.valueobject;

import it.unicam.cs.ids.hackhub.domain.enums.StaffRole;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class StaffStructureTest {
    @Test
    public void rejectsMultipleRolesForSameUser() {
        UserId userId = new UserId(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> new StaffStructure(List.of(
                new StaffAssignment(userId, StaffRole.ORGANIZER),
                new StaffAssignment(userId, StaffRole.JUDGE)
        )));
    }

    @Test
    public void allowsDifferentUsersToHaveStaffRoles() {
        StaffStructure staffStructure = new StaffStructure(List.of(
                new StaffAssignment(new UserId(UUID.randomUUID()), StaffRole.ORGANIZER),
                new StaffAssignment(new UserId(UUID.randomUUID()), StaffRole.JUDGE)
        ));

        assertEquals(2, staffStructure.assignments().size());
    }
}
