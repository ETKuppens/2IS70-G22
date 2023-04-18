package com.example.cardhub.authentication;

/**
 * State Design Pattern implementation for signing in users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class LoginState implements LoginReceiver {
    // Variables
    private final LoginActivity activity;

    /**
     * Constructs new LoginState instance using the given {@code activity}
     * instance.
     *
     * @param activity LoginActivity instance to be referenced in the code
     * @pre {@code activity != null}
     * @throws NullPointerException if {@code activity == null}
     * @post instance is initialized
     */
    public LoginState(LoginActivity activity) throws NullPointerException {
        // Precondition testing
        // Activity precondition test
        if (activity == null) {
            throw new NullPointerException(
                    "LoginState.LoginState.pre violated: activity == null"
            );
        }

        this.activity = activity;
    }

    @Override
    public void signInSuccess(String role) throws NullPointerException, IllegalArgumentException {
        if (role == null) {
            throw new NullPointerException("LoginState.signInSuccess.pre violated: role == null");
        } else if (role.equals("Card Collector")) {
            activity.signInSuccessCollector();
        } else if (role.equals("Card Creator")) {
            activity.signInSuccessCreator();
        } else {
            throw new IllegalArgumentException("LoginState.signInSuccess.pre violated: role == " + role);
        }
    }

    /**
     * Passes the sign-in request to the Data Design Pattern class.
     *
     * @param emailAddress emailAddress to sign in with
     * @param password password to sign in with
     * @pre {@code emailAddress != null && password != null}
     * @throws NullPointerException if {emailAddress == null || password == null}
     * @post parameters have been passed for signing in
     */
    public void signIn(String emailAddress, String password) throws NullPointerException {
        // Precondition testing
        // EmailAddress precondition test
        if (emailAddress == null) {
            throw new NullPointerException("LoginState.signIn.pre violated: emailAddress == null");
        }

        // Password precondition test
        if (password == null) {
            throw new NullPointerException("LoginState.signIn.pre violated: password == null");
        }

        // Credential testing
        // Variables
        final boolean isProperPassword = !password.isEmpty();
        final boolean isProperEmail = !emailAddress.isEmpty();

        // Check if the emailAddress is not empty.
        if (!isProperEmail) {
            activity.signInEmailStringFail();
            return;
        }

        // Check if the password is not empty
        if (!isProperPassword) {
            activity.signInPasswordStringFail();
            return;
        }

        // Attempt to sign-in
        // Variables
        final LoginData data = new LoginData(this);

        data.signIn(emailAddress, password); // Pass sign-in request
    }

    @Override
    public void signInDatabaseFail() {
        activity.signInDatabaseFail();
    }

    /**
     * Passes the session-check request to the Data Design Pattern class.
     * @post session-check function has been called
     */
    public void signInSignedInUsers() {
        // Variables
        final LoginData data = new LoginData(this);

        data.signInSignedInUsers(); // Pass session-check request
    }
}
