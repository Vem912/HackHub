package it.unicam.cs.ids.hackhub.domain.entity;

import it.unicam.cs.ids.hackhub.domain.valueobject.PaymentDetails;
import it.unicam.cs.ids.hackhub.domain.valueobject.UserId;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class TeamTest {
    @Test
    public void createsTeamWithCreatorAsItsOnlyMember() {
        UserId creatorId = new UserId(UUID.randomUUID());
        PaymentDetails paymentDetails = new PaymentDetails("IBAN: example");

        Team team = Team.create("Team name", creatorId, paymentDetails);

        assertNotNull(team.id());
        assertEquals("Team name", team.name());
        assertEquals(List.of(creatorId), team.members());
        assertEquals(paymentDetails, team.paymentDetails());
    }

    @Test
    public void allowsPaymentDetailsToBeChanged() {
        Team team = Team.create(
                "Team name",
                new UserId(UUID.randomUUID()),
                new PaymentDetails("Initial details"));
        PaymentDetails updatedDetails = new PaymentDetails("Updated details");

        team.changePaymentDetails(updatedDetails);

        assertEquals(updatedDetails, team.paymentDetails());
    }

    @Test
    public void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> Team.create(
                " ",
                new UserId(UUID.randomUUID()),
                new PaymentDetails("Payment details")));
    }
}
