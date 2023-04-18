package com.example.cardhub.authentication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private ForgotPasswordReceiver receiver;

    /**
     * Constructs a new ForgotPasswordData instance using the given {@code receiver}
     * instance.
     *
     * @param receiver given receiver instance
     *
     * @pre {@code receiver != null}
     *
     * @throws NullPointerException if {@code receiver == null}
     */
    public ForgotPasswordData(ForgotPasswordReceiver receiver) {
        if (receiver == null) {
            throw new NullPointerException(
                    "ForgotPasswordData.ForgotPasswordData.pre violated: receiver == null"
            );
        }

        this.receiver = receiver;
    }

    /**
     * Sends password-reset request to {@code emailAddress}.
     *
     * @pre {@code emailAddress != null}
     *
     * @param emailAddress emailAddress which the password-reset request shall be send to
     *
     * @throws NullPointerException if {@code emailAddress == null}
     */
    public void sendForgotPasswordEmail(String emailAddress) {
        if (emailAddress == null) {
            throw new NullPointerException("ForgotPasswordData.ForgotPasswordData.pre violated: emailAddress == null");
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Attempt to send a password-reset request
        mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) { // Email was send successfully
                    // Log success
                    Log.d(TAG, "sendPasswordResetEmail:success");

                    receiver.sendForgotPasswordEmailSuccess(); // Propagate success signal
                } else { // Email was not send successfully
                    Log.w(TAG, "sendPasswordResetEmail:failure",
                            task.getException()); // Log failure

                    receiver.sendForgotPasswordEmailFailure(); // Propagate failure signal
                }
            }
        });
    }
}
