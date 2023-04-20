package com.example.cardhub.authentication;

import org.junit.Test;

public class RegistrationStateTest {
    /**
     * Test that {@code RegistrationState} propagates a NullPointerException when {@code activity == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationStateNull() {
        new RegistrationState(null);
    }
}