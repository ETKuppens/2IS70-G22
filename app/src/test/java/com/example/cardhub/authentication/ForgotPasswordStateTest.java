package com.example.cardhub.authentication;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Tests ForgotPasswordState.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 20-04-2023
 */
public class ForgotPasswordStateTest {
    // Variables
    @Mock
    private ForgotPasswordActivity activity;
    @Mock
    private ForgotPasswordData data;
    private final ForgotPasswordState state;

    public ForgotPasswordStateTest() {
        MockitoAnnotations.openMocks(this);
        this.state = new ForgotPasswordState(activity, data);
    }


    /**
     * Test that {@code ForgotPasswordState} propagates a NullPointerException when {@code activity == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testForgotPasswordStateNull0() {
        new ForgotPasswordState(null);
    }

    /**
     * Test that {@code ForgotPasswordState} propagates a NullPointerException when {@code activity == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testForgotPasswordStateNull1() {
        new ForgotPasswordState(null, null);
    }

    /**
     * Test that {@code ForgotPasswordState} propagates a NullPointerException when {@code activity == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testForgotPasswordStateNull2() {
        new ForgotPasswordState(activity, null);
    }

    /**
     * Test that {@code ForgotPasswordState} propagates a NullPointerException when {@code activity == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testForgotPasswordStateNull3() {
        new ForgotPasswordState(null, data);
    }

    /**
     * Test that {@code sendForgotPasswordEmailSuccess} calls {@code sendForgotPasswordEmailSuccess} on {@code activity}.
     */
    @Test
    public void testSendForgotPasswordEmailSuccess() {
        // Act
        state.sendForgotPasswordEmailSuccess();

        // Verify
        Mockito.verify(activity).sendForgotPasswordEmailSuccess();
    }

    /**
     * Test that {@code sendForgotPasswordEmailDatabaseFailure} calls {@code sendForgotPasswordEmailDatabaseFailure} on {@code activity}.
     */
    @Test
    public void testSendForgotPasswordEmailDatabaseFailure() {
        // Act
        state.sendForgotPasswordEmailDatabaseFailure();

        // Verify
        Mockito.verify(activity).sendForgotPasswordEmailDatabaseFailure();
    }

    /**
     * Test that {@code sendForgotPasswordEmail} propagates a NullPointerException when {@code emailAddress == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testSendForgotPasswordEmailNull0() {
        state.sendForgotPasswordEmail(null);
    }

    /**
     * Test that {@code sendForgotPasswordEmail} propagates a NullPointerException when {@code emailAddress.equals("")}.
     */
    @Test(expected = NullPointerException.class)
    public void testSendForgotPasswordEmailNull1() {
        state.sendForgotPasswordEmail("");
    }

    /**
     * Test that {@code testSendForgotPasswordEmail} calls {@code sendForgotPasswordEmail} on {@code data}.
     */
    @Test
    public void testSendForgotPasswordEmail() {
        // Variables
        final String emailAddress = "rebuilded2.0@gmail.com";

        // Act
        state.sendForgotPasswordEmail(emailAddress);

        // Verify
        Mockito.verify(data).sendForgotPasswordEmail(emailAddress);
    }
}