package it.unicam.cs.ids.hackhub.domain.valueobject;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class PaymentDetailsTest {
    @Test
    public void rejectsBlankValue() {
        assertThrows(IllegalArgumentException.class, () -> new PaymentDetails("  "));
    }
}
