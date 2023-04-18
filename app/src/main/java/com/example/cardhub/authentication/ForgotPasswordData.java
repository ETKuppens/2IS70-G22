package com.example.cardhub.authentication;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Data Design Pattern implementation for sending password-reset-emails to users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class ForgotPasswordData {
    // Constants
    public static final String TAG = "ForgotPasswordData";

    // Variables
    private final ForgotPasswordReceiver receiver;

    /**
     * Constructs a new ForgotPasswordData instance using the given {@code receiver}
     * instance.
     *
     * @param receiver given receiver instance
     * @pre {@code receiver != null}
     * @throws NullPointerException if {@code receiver == null}
     * @post instance is initialized
     */
    public ForgotPasswordData(ForgotPasswordReceiver receiver) {
        // Precondition testing
        // Receiver precondition test
        if (receiver == null) {
            throw new NullPointerException(
                    "ForgotPasswordData.ForgotPasswordData.pre violated: receiver == null"
            );
        }

        this.receiver = receiver;
    }

    /**
     * Sends password-reset-email to {@code emailAddress}.
     *
     * @pre {@code emailAddress != null}
     * @param emailAddress emailAddress which the password-reset request shall be send to
     * @throws NullPointerException if {@code emailAddress == null}
     * @post password-reset email has been send
     */
    public void sendForgotPasswordEmail(String emailAddress) {
        // Precondition testing
        // EmailAddress precondition test
        if (emailAddress == null) {
            throw new NullPointerException("ForgotPasswordData.ForgotPasswordData.pre violated: emailAddress == null");
        }

        // Attempt to send a password-reset request
        FirebaseAuth.getInstance()
                .sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(task -> {
            if (task.isSuccessful()) { // Email was send successfully
                Log.d(TAG, "sendPasswordResetEmail:success"); // Log success
                receiver.sendForgotPasswordEmailSuccess(); // Propagate success signal
            } else { // Email was not send successfully
                Log.w(TAG, "sendPasswordResetEmail:failure", task.getException()); // Log failure
                receiver.sendForgotPasswordEmailFailure(); // Propagate failure signal
            }
        });
    }
}
