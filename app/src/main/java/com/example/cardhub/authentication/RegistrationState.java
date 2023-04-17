package com.example.cardhub.authentication;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

/**
 * State Design Pattern implementation for the registering of the users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class RegistrationState implements RegisterReceiver {
    // Variables
    private final RegistrationData data;
    private final RegistrationActivity activity;

    /**
     * Constructs new RegistrationState instance using the given {@code activity}.
     *
     * @param activity RegistrationActivity instance to be referenced in the code
     *
     * @pre {@code activity != null}
     *
     * @throws NullPointerException if {@code activity == null}
     */
    public RegistrationState(RegistrationActivity activity) throws NullPointerException {
        if (activity == null) {
            throw new NullPointerException(
                    "RegistrationState.RegistrationState.pre violated: activity == null"
            );
        }

        this.activity = activity;
        this.data = new RegistrationData(this);
    }

    /**
     * Registers the client using the given {@code email}, {@code password},
     * and {@code role}.
     *
     * @param email email to be registered with
     * @param password password to register with
     * @param role role to register with
     *
     * @throws NullPointerException if {@code email == null || password == null || role == null}
     * @throws IllegalArgumentException if {@code !role.equals("Card Creator") && !role.equals("Card Collector")}
     *
     * @pre {@code email != null && password != null && (role.equals('Card Creator') ^ role.equals('Card Collector'))
     *      && password.equals(confirm)}
     */
    public void register(String email, String password, String confirm, String role) throws NullPointerException, IllegalArgumentException {
        if (email == null) {
            throw new NullPointerException("RegistrationState.register.pre violated: email == null");
        }

        if (password == null) {
            throw new NullPointerException("RegistrationState.register.pre violated: password == null");
        }

        if (role == null) {
            throw new NullPointerException("RegistrationState.register.pre violated: role == null");
        }

        if (!role.equals("Card Creator") && !role.equals("Card Collector")) {
            throw new IllegalArgumentException("RegistrationState.register.pre violated: !role.equals(\"Card Creator\") && !role.equals(\"Card Collector\")");
        }

        if (password.equals(confirm)) {

        }

        data.register(email, password, role); // Account Creation
    }

    /**
     * Passes the registration success to the {@code activity}
     * using the {@code role} such that the activity is started.
     *
     * @param role role of the the user that has been successfully registered
     *
     * @pre {@code (role.equals('Card Creator') ^ role.equals('Card Collector')) && activity != null}
     *
     * @throws NullPointerException if {@code role == null || activity == null}
     * @throws IllegalArgumentException if {@code !role.equals('Card Creator') && !role.equals('Card Collector')}
     */
    @Override
    public void registrationSuccess(String role) throws NullPointerException, IllegalArgumentException {
        if (role == null) {
            throw new NullPointerException("RegistrationState.registrationSuccess violated: role == null");
        }

        if (activity == null) {
            throw new NullPointerException("RegistrationState.registrationSuccess violated: activity == null");
        }

        if (!(role.equals("Card Creator") ^ role.equals("Card Collector"))) {
            throw new IllegalArgumentException("RegistrationState.registrationSuccess.pre violated: !role.equals('Card Creator') && !role.equals('Card Collector')");
        }

        activity.openStartActivity(role); // Open the appropriate activity
    }

    /**
     * Displays a registration failure toast in the given {@code activity}.
     *
     * @pre {@code activity != null}
     *
     * @throws NullPointerException if {@code activity == null}
     */
    @Override
    public void registrationDatabaseFail() throws NullPointerException {
        if (activity == null) {
            throw new NullPointerException("RegistrationState.registrationFail.pre violated: activity == null");
        }

        Toast.makeText(activity.getApplicationContext(), "Registration failed, try again later!",
                Toast.LENGTH_SHORT).show();
    }
}
