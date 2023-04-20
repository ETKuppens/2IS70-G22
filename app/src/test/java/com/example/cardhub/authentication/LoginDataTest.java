package com.example.cardhub.authentication;

import org.junit.Test;

public class LoginDataTest {
    /**
     * Test that {@code LoginData} propagates a NullPointerException when {@code receiver == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginDataNull() {
        new LoginData(null);
    }
}