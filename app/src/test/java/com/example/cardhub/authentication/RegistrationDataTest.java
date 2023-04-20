package com.example.cardhub.authentication;

import org.junit.Test;

public class RegistrationDataTest{
    /**
     * Test that {@code RegistrationData} propagates a NullPointerException when {@code receiver == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationDataNull() {
        new RegistrationData(null);
    }
}