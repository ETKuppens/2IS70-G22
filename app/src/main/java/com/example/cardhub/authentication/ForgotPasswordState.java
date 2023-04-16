package com.example.cardhub.authentication;

/**
 * State Design Pattern implementation for the password-reset
 * mailing to users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class ForgotPasswordState implements ForgotPasswordReceiver {
    // Variables
    private ForgotPasswordActivity activity;

    /**
     * Constructs a new ForgotPasswordState instance using the given {@code activity}
     * instance.
     *
     * @param activity ForgotPasswordActivity instance to be referenced in the code
     *
     * @pre {@code activity != null}
     *
     * @throws NullPointerException if {@code activity == null}
     */
    public ForgotPasswordState(ForgotPasswordActivity activity) throws NullPointerException {
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
    public void sendForgotPasswordEmailFailure() {
        activity.sendForgotPasswordEmailFailure();
    }

    /**
     * Passes password-reset email of {@code emailAddress} to {@code data}.
     *
     * @pre {@code emailAddress != null}
     *
     * @param emailAddress emailAddress which the password-reset request shall be send to
     *
     * @throws NullPointerException if {@code emailAddress == null}
     */
    public void sendForgotPasswordEmail(String emailAddress) {
        if (emailAddress == null) {
            throw new NullPointerException("ForgotPasswordState.sendForgotPasswordEmail.pre violated: emailAddress == null");
        }

        // Variables
        ForgotPasswordData data = new ForgotPasswordData(this);

        data.sendForgotPasswordEmail(emailAddress); // Pass password-reset request
    }
}
