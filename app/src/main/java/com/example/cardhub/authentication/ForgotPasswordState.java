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
    public void sendForgotPasswordEmail(String emailAddress) {
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
            activity.registrationEmailStringFail();
            return;
        }

        // Send password-reset-email request
        // Variables
        final ForgotPasswordData data = new ForgotPasswordData(this);

        data.sendForgotPasswordEmail(emailAddress); // Pass password-reset request
    }
}
