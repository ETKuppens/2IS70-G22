package com.example.cardhub.authentication;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    /**
     * Test that {@code signInSuccess} calls {@code signInSuccessCollector} on {@code activity}
     * when {@code role.equals("Card Collector")}.
     */
    @Test
    public void testSignInSuccessCollector() {
        // Act
        state.signInSuccess("Card Collector");

        // Verify
        Mockito.verify(activity).signInSuccessCollector();
    }

    /**
     * Test that {@code signInSuccess} calls {@code signInSuccessCreator} on {@code activity}
     * when {@code role.equals("Card Creator")}.
     */
    @Test
    public void testSignInSuccessCreator() {
        // Act
        state.signInSuccess("Card Creator");

        // Verify
        Mockito.verify(activity).signInSuccessCreator();
    }

    /**
     * Test that {@code signInSuccess} propagates a NullException when {@code role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testSignInSuccessNullException() {
        state.signInSuccess(null);
    }

    /**
     * Test that {@code signInSuccess} propagates a IllegalArgumentException when  {@code role != null && !(role.equals("Card Collector") ^ role.equals("Card Creator"))}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSignInSuccessIllegalArgumentException() {
        state.signInSuccess("Mistake");
    }

    /**
     * Test that {@code signInDatabaseFail} calls {@code signInDatabaseFail} on {@code activity}.
     */
    @Test
    public void testSignInDatabaseFail() {
        // Act
        state.signInDatabaseFail();

        // Verify
        Mockito.verify(activity).signInDatabaseFail();
    }

    /**
     * Test that {@code signInSignedInUsers} calls {@code signInSignedInUsers} on {@code data}.
     */
    @Test
    public void testSignInSignedInUsersFail() {
        // Act
        state.signInSignedInUsers();

        // Verify
        Mockito.verify(data).signInSignedInUsers();
    }

    /**
     * Test that {@code signIn} calls {@code signIn} on {@code data} when given proper parameters.
     */
    @Test
    public void testSignIn() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "testtest";

        // Act
        state.signIn(emailAddress, password);

        // Verify
        Mockito.verify(data).signIn(emailAddress, password);
    }

    /**
     * Test that {@code signIn} propagates a NullException when {@code emailAddress == null && password != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testSignInNullException0() {
        state.signIn(null, "testtest");
    }

    /**
     * Test that {@code signIn} propagates a NullException when {@code emailAddress != null && password == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testSignInNullException1() {
        state.signIn("a.oostenbrug@student.tue.nl", null);
    }

    /**
     * Test that {@code signIn} propagates a NullException when {@code emailAddress == null && password == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testSignInNullException2() {
        state.signIn(null, null);
    }

    /**
     * Test that {@code signIn} calls {@code signInEmailStringFail} on {@code activity} when {@code emailAddress.isEmpty()}.
     */
    @Test
    public void testRegisterEmailStringFail() {
        // Variables
        final String emailAddress = "";
        final String password = "testtest";

        // Act
        state.signIn(emailAddress, password);

        // Verify
        Mockito.verify(activity).signInEmailStringFail();
    }

    /**
     * Test that {@code signIn} calls {@code signInPasswordStringFail} on {@code activity} when {@code password.isEmpty()}.
     */
    @Test
    public void testRegisterPasswordStringFail() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "";

        // Act
        state.signIn(emailAddress, password);

        // Verify
        Mockito.verify(activity).signInPasswordStringFail();
    }
}