package com.example.cardhub.authentication;

import org.junit.Test;

public class ForgotPasswordDataTest {

    /**
     * Test that {@code ForgotPasswordData} propagates a NullPointerException when {@code receiver == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testForgotPasswordDataNull() {
        new ForgotPasswordData(null);
    }
}