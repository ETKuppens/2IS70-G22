package com.example.cardhub.authentication;

/**
 * State Design Pattern implementation for the registration users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class RegistrationState implements RegistrationReceiver {
    // Constants
    static final int PASSWORD_LENGTH = 6;

    // Variables
    private final RegistrationActivity activity;
    private final RegistrationData data;

    /**
     * Constructs new RegistrationState instance using the given {@code activity} instance.
     *
     * @param activity RegistrationActivity instance to be referenced in the code
     * @pre {@code activity != null}
     * @throws NullPointerException if {@code activity == null}
     * @post instance is initialized
     */
    public RegistrationState(RegistrationActivity activity) throws NullPointerException {
        // Precondition testing
        // Activity precondition test
        if (activity == null) {
            throw new NullPointerException(
                    "RegistrationState.RegistrationState.pre violated: activity == null"
            );
        }

        this.activity = activity;
        this.data = new RegistrationData(this);
    }

    /**
     * Constructs new RegistrationState instance using the given {@code activity},
     * and {@code data} instances.
     *
     * @param activity RegistrationActivity instance to be referenced in the code
     * @param data RegistrationData instance to be referenced in the code
     * @pre {@code activity != null && data != null}
     * @throws NullPointerException if {@code activity == null || data == null}
     * @post instance is initialized
     */
    public RegistrationState(RegistrationActivity activity, RegistrationData data) throws NullPointerException {
        // Precondition testing
        // Activity precondition test
        if (activity == null) {
            throw new NullPointerException(
                    "RegistrationState.RegistrationState.pre violated: activity == null"
            );
        }

        if (data == null) {
            throw new NullPointerException(
                    "RegistrationState.RegistrationState.pre violated: data == null"
            );
        }

        this.activity = activity;
        this.data = data;
    }

    /**
     * Passes registration requests using {@code emailAddress}, {@code password},
     * and {@code role}.
     *
     * @param emailAddress emailAddress to be registered with
     * @param password password to register with
     * @param role role to register with
     * @throws IllegalArgumentException if {@code role != null && !(role.equals("Card Creator") ^ role.equals("Card Collector"))}
     * @throws NullPointerException if {@code emailAddress == null || password == null || confirm == null || role == null}
     * @pre {@code emailAddress != null && password != null && confirm != null && role != null & (role.equals('Card Creator') ^ role.equals('Card Collector'))}
     * @post parameters have been passed for registration
     */
    public void register(String emailAddress, String password, String confirm, String role) throws NullPointerException, IllegalArgumentException {
        // Precondition testing
        // Email precondition test
        if (emailAddress == null) {
            throw new NullPointerException("RegistrationState.register.pre violated: emailAddress == null");
        }

        // Password precondition test
        if (password == null) {
            throw new NullPointerException("RegistrationState.register.pre violated: password == null");
        }

        // Confirm precondition test
        if (confirm == null) {
            throw new NullPointerException("RegistrationState.register.pre violated: confirm == null");
        }

        // Role precondition tests
        if (role == null) {
            throw new NullPointerException("RegistrationState.register.pre violated: role == null");
        }

        if (!((role.equals("Card Creator") ^ role.equals("Card Collector")))) {
            throw new IllegalArgumentException("RegistrationState.register.pre violated: !role.equals(\"Card Creator\") && !role.equals(\"Card Collector\")");
        }
        
        // Credential testing
        // Variables
        final boolean isProperEmail = !emailAddress.isEmpty();
        final boolean hasAdequatePasswordLength = password.length() >= PASSWORD_LENGTH;
        final boolean isPasswordMatching = password.equals(confirm);

        // Check if the emailAddress is not empty
        if (!isProperEmail) {
            activity.registrationEmailStringFail();
            return;
        }

        // Check if the password is long enough
        if (!hasAdequatePasswordLength) {
            activity.registrationPasswordLengthFail();
            return;
        }
                
        // Check if the password has been properly re-typed.
        if (!isPasswordMatching) {
            activity.registrationConfirmationFail();
            return;
        }

        // Send registration request
        data.register(emailAddress, password, role); // Passes registration request
    }

    @Override
    public void registrationSuccess(String role) throws NullPointerException, IllegalArgumentException {
        if (role == null) {
            throw new NullPointerException("RegistrationState.registrationSuccess.pre violated: role == null");
        } else if (role.equals("Card Collector")) {
            activity.registrationSuccessCollector();
        } else if (role.equals("Card Creator")) {
            activity.registrationSuccessCreator();
        } else {
            throw new IllegalArgumentException("RegistrationState.registrationSuccess.pre violated: role == " + role);
        }
    }

    @Override
    public void registrationDatabaseFail() {
        activity.registrationDatabaseFail();
    }
}
