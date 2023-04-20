package com.example.cardhub.authentication;

import org.junit.Test;

public class LoginStateTest {
    /**
     * Test that {@code LoginState} propagates a NullPointerException when {@code activity == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginStateNull() {
        new LoginState(null);
    }
}