package com.example.cardhub.authentication;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

/**
 * Interface for the State Design pattern implementation of the registration of
 * users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public interface RegisterReceiver {
    /**
     * Passes the registration success signal.
     *
     * @param role role of the the user that has been successfully registered
     */
    public void registrationSuccess(String role);

    /**
     * Passes the registration database-failure signal.
     */
    public void registrationDatabaseFail();
}
