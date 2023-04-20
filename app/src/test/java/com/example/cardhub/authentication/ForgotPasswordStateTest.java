package com.example.cardhub.authentication;

import org.junit.Test;

public class ForgotPasswordStateTest {
    /**
     * Test that {@code ForgotPasswordState} propagates a NullPointerException when {@code activity == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testForgotPasswordStateNull() {
        new ForgotPasswordState(null);
    }
}