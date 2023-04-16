package com.example.cardhub.authentication;

import com.google.firebase.auth.FirebaseUser;

/**
 * Interface for the State Design pattern implementation of the signing in
 * of users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public interface LoginReceiver {

    /**
     * Takes appropriate action upon the successful signing in of the
     * {@code user}.
     *
     * @param user user account that has been signed in to successfully
     */
    public void signInSuccess(FirebaseUser user);

    /**
     * Takes appropriate action upon the unsuccessful signing in of the
     * {@code user}.
     */
    public void signInFail();

    /**
     * Launches the appropriate start activity for the given {@code role}.
     *
     * @param role role of the user
     */
    public void userRoleCallback(String role);

    /**
     * Receives the {@code user} account upon signing in.
     *
     * @param user given user account
     */
    public void receiveCurrentUser(FirebaseUser user);

}
