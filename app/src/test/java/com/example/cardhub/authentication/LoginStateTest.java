package com.example.cardhub.authentication;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tests LoginState.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 21-04-2023
 */
public class LoginStateTest {
    // Variables
    @Mock
    private LoginActivity activity;
    @Mock
    private LoginData data;
    @Mock
    private final LoginState state;

    public LoginStateTest() {
        MockitoAnnotations.openMocks(this);
        this.state = new LoginState(activity, data);
    }

    /**
     * Test that {@code LoginState} propagates a NullPointerException when {@code activity == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginStateNullException0() {
        new LoginState(null);
    }

    /**
     * Test that {@code LoginState} propagates a NullPointerException when {@code activity == null && data == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginStateNullException1() {
        new LoginState(null, null);
    }

    /**
     * Test that {@code LoginState} propagates a NullPointerException when {@code activity != null && data == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginStateNullException2() {
        new LoginState(activity, null);
    }

    /**
     * Test that {@code LoginState} propagates a NullPointerException when {@code activity == null && data != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginStateNullException3() {
        new LoginState(null, data);
    }
}