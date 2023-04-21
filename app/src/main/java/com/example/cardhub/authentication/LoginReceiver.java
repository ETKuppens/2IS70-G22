package com.example.cardhub.authentication;

/**
 * Interface for the State Design Pattern implementation for signing in users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public interface LoginReceiver {
    /**
     * Propagates sign-in-success signals to the function associated with {@code role}.
     *
     * @param role role of the the user that has been successfully registered
     * @pre {@code (role.equals("Card Collector") ^ role.equals("Card Creator"))}
     * @throws NullPointerException if {@code role == null}
     * @throws IllegalArgumentException if {@code !((role.equals("Card Collector") ^ role.equals("Card Creator"))))}
     * @post correct registration-success callback function has been called
     */
    void signInSuccess(String role) throws NullPointerException, IllegalArgumentException;

    /**
     * Propagates sign-in-database-failure signals.
     *
     * @post sign-in-database-failure function has been called
     */
    void signInDatabaseFail();
}
