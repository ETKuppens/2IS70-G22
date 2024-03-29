package com.example.cardhub.authentication;

/**
 * State Design Pattern implementation for sending password-reset-emails to users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class ForgotPasswordState implements ForgotPasswordReceiver {
    // Variables
    private final ForgotPasswordActivity activity;
    private final ForgotPasswordData data;

    /**
     * Constructs new ForgotPasswordState instance using the given {@code activity}
     * instance.
     *
     * @param activity ForgotPasswordActivity instance to be referenced in the code
     * @pre {@code activity != null}
     * @throws NullPointerException if {@code activity == null}
     * @post instance is initialized
     */
    public ForgotPasswordState(ForgotPasswordActivity activity) throws NullPointerException {
        // Precondition testing
        // Activity precondition test
        if (activity == null) {
            throw new NullPointerException(
                    "ForgotPasswordState.ForgotPasswordState.pre violated: activity == null"
            );
        }

        this.activity = activity;
        this.data = new ForgotPasswordData(this);
    }

    /**
     * Constructs new ForgotPasswordState instance using the given {@code activity},
     * and {@code data} instances.
     *
     * @param activity ForgotPasswordActivity instance to be referenced in the code
     * @param data ForgotPasswordData instance to be referenced in the code
     * @pre {@code activity != null && data != null}
     * @throws NullPointerException if {@code activity == null || data == null}
     * @post instance is initialized
     */
    public ForgotPasswordState(ForgotPasswordActivity activity, ForgotPasswordData data) throws NullPointerException {
        // Precondition testing
        // Activity precondition test
        if (activity == null) {
            throw new NullPointerException(
                    "ForgotPasswordState.ForgotPasswordState.pre violated: activity == null"
            );
        }

        // Data precondition test
        if (data == null) {
            throw new NullPointerException(
                    "ForgotPasswordState.ForgotPasswordState.pre violated: data == null"
            );
        }

        this.activity = activity;
        this.data = data;
    }

    @Override
    public void sendForgotPasswordEmailSuccess() {
        activity.sendForgotPasswordEmailSuccess();
    }

    @Override
    public void sendForgotPasswordEmailDatabaseFailure() {
        activity.sendForgotPasswordEmailDatabaseFailure();
    }

    /**
     * Passes password-reset-email requests of {@code emailAddress}.
     *
     * @pre {@code emailAddress != null}
     * @param emailAddress emailAddress which the password-reset request shall be send to
     * @throws NullPointerException if {@code emailAddress == null}
     * @post parameters have been passed for sending the password-reset-email
     */
    public void sendForgotPasswordEmail(String emailAddress) throws NullPointerException {
        // Precondition testing
        // EmailAddress precondition test
        if (emailAddress == null) {
            throw new NullPointerException("ForgotPasswordState.sendForgotPasswordEmail.pre violated: emailAddress == null");
        }

        // Credential testing
        // Variables
        final boolean isProperEmail = !emailAddress.isEmpty();

        // Check if the emailAddress is not empty
        if (!isProperEmail) {
            activity.sendForgotPasswordEmailStringFailure();
            return;
        }

        // Send password-reset-email request
        data.sendForgotPasswordEmail(emailAddress); // Pass password-reset request
    }
}
