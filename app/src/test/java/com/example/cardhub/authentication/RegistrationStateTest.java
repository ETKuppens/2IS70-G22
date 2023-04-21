package com.example.cardhub.authentication;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Tests RegistrationState.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 21-04-2023
 */
public class RegistrationStateTest {
    // Variables
    @Mock
    private RegistrationActivity activity;
    @Mock
    private RegistrationData data;
    private final RegistrationState state;

    public RegistrationStateTest() {
        MockitoAnnotations.openMocks(this);
        this.state = new RegistrationState(activity, data);
    }

    /**
     * Test that {@code RegistrationState} propagates a NullPointerException when {@code activity == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationStateNullException0() {
        new RegistrationState(null);
    }

    /**
     * Test that {@code RegistrationState} propagates a NullPointerException when {@code activity == null && data == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationStateNullException1() {
        new RegistrationState(null, null);
    }

    /**
     * Test that {@code RegistrationState} propagates a NullPointerException when {@code activity != null && data == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationStateNullException2() {
        new RegistrationState(activity, null);
    }

    /**
     * Test that {@code RegistrationState} propagates a NullPointerException when {@code activity == null && data != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationStateNullException3() {
        new RegistrationState(null, data);
    }

    /**
     * Test that {@code registrationSuccess} calls {@code registrationSuccessCollector} on {@code activity}
     * when {@code role.equals("Card Collector")}.
     */
    @Test
    public void testRegistrationSuccessCollector() {
        // Act
        state.registrationSuccess("Card Collector");

        // Verify
        Mockito.verify(activity).registrationSuccessCollector();
    }

    /**
     * Test that {@code registrationSuccess} calls {@code registrationSuccessCreator} on {@code activity}
     * when {@code role.equals("Card Creator")}.
     */
    @Test
    public void testRegistrationSuccessCreator() {
        // Act
        state.registrationSuccess("Card Creator");

        // Verify
        Mockito.verify(activity).registrationSuccessCreator();
    }

    /**
     * Test that {@code registrationDatabaseFail} calls {@code registrationDatabaseFail} on {@code activity}.
     */
    @Test
    public void testRegistrationDatabaseFail() {
        // Act
        state.registrationDatabaseFail();

        // Verify
        Mockito.verify(activity).registrationDatabaseFail();
    }

    /**
     * Test that {@code registrationSuccess} propagates a NullException when {@code role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationSuccessNullException() {
        state.registrationSuccess(null);
    }

    /**
     * Test that {@code registrationSuccess} propagates a IllegalArgumentException when {@code role != null && !(role.equals("Card Collector") ^ role.equals("Card Creator"))}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrationSuccessIllegalArgument() {
        state.registrationSuccess("Mistake");
    }

    /**
     * Test that {@code register} calls {@code register} on {@code data} when given proper parameters.
     */
    @Test
    public void testRegister() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "testtest";
        final String confirm = "testtest";
        final String role = "Card Collector";

        // Act
        state.register(emailAddress, password, confirm, role);

        // Verify
        Mockito.verify(data).register(emailAddress, password, role);
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress == null && password != null && confirm != null && role != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException0() {
       state.register(null, "testtest", "testtest", "Card Collector");
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress != null && password == null && confirm != null && role != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException1() {
        state.register("a.oostenbrug@student.tue.nl", null, "testtest", "Card Collector");
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress != null && password != null && confirm == null && role != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException2() {
        state.register("a.oostenbrug@student.tue.nl", "testtest", null, "Card Collector");
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress != null && password != null && confirm != null && role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException3() {
        state.register("a.oostenbrug@student.tue.nl", "testtest", "testtest", null);
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress == null && password == null && confirm != null && role != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException4() {
        state.register(null, null, "testtest", "Card Collector");
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress != null && password == null && confirm == null && role != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException5() {
       state.register("a.oostenbrug@student.tue.nl", null, null, "Card Collector");
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress != null && password != null && confirm == null && role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException6() {
        state.register("a.oostenbrug@student.tue.nl", "testtest", null, null);
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress == null && password != null && confirm == null && role != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException7() {
        state.register(null, "testtest", null, "Card Collector");
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress != null && password == null && confirm != null && role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException8() {
        state.register("a.oostenbrug@student.tue.nl", null, "testtest", null);
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress == null && password != null && confirm != null && role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException9() {
        state.register(null, "testtest", "testtest", null);
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress != null && password == null && confirm == null && role != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException10() {
        state.register("a.oostenbrug@student.tue.nl", null, null, "Card Collector");
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress == null && password == null && confirm == null && role != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException11() {
        state.register(null, null, null, "Card Collector");
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress == null && password == null && confirm != null && role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException12() {
        state.register(null, null, "testtest", null);
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress == null && password != null && confirm == null && role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException13() {
        state.register(null, "testtest", null, null);
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress != null && password == null && confirm == null && role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException14() {
        state.register("a.oostenbrug@student.tue.nl", null, null, null);
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress == null && password == null && confirm == null && role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException15() {
        state.register(null, null, null, null);
    }

    /**
     * Test that {@code register} propagates an IllegalArgumentException when {@code role != null && !(role.equals("Card Creator") ^ role.equals("Card Collector")}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterIllegalArgument() {
        state.register("a.oostenbrug@student.tue.nl", "testtest", "testtest", "Mistake");
    }

    /**
     * Test that {@code register} calls {@code registrationEmailStringFail} on {@code activity} when {@code emailAddress.isEmpty()}.
     */
    @Test
    public void testRegisterEmailStringFail() {
        // Variables
        final String emailAddress = "";
        final String password = "testtest";
        final String confirm = "testtest";
        final String role = "Card Collector";

        // Act
        state.register(emailAddress, password, confirm, role);

        // Verify
        Mockito.verify(activity).registrationEmailStringFail();
    }

    /**
     * Test that {@code register} calls {@code registrationPasswordLengthFail} on {@code activity} when {@code password >= PASSWORD_LENGTH}.
     */
    @Test
    public void testRegistrationPasswordLengthFail() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "short";
        final String confirm = "short";
        final String role = "Card Collector";

        // Act
        state.register(emailAddress, password, confirm, role);

        // Verify
        Mockito.verify(activity).registrationPasswordLengthFail();
    }

    /**
     * Test that {@code register} calls {@code registrationConfirmationFail} on {@code activity} when {@code password != confirm} && password != null && confirm != null.
     */
    @Test
    public void registrationConfirmationFail() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "abcabc";
        final String confirm = "defdef";
        final String role = "Card Collector";

        // Act
        state.register(emailAddress, password, confirm, role);

        // Verify
        Mockito.verify(activity).registrationConfirmationFail();
    }
}