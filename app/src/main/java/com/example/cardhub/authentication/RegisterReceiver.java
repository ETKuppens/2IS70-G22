package com.example.cardhub.authentication;

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
     * Takes appropriate action upon the successful registration of the
     * {@code user}.
     *
     * @param user user that has been successfully registered
     * @param role role of the the user that has been successfully registered
     */
    public void registrationSuccess(FirebaseUser user, String role);

    /**
     * Takes appropriate action upon the unsuccessful registration of the
     * {@code user}.
     */
    public void registrationFail();
}
