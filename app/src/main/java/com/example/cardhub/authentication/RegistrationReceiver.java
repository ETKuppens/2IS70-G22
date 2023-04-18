package com.example.cardhub.authentication;

/**
 * Interface for the State Design pattern implementation of the registration of
 * users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public interface RegistrationReceiver {
    /**
     * Propagates registration-success signals to the function associated with {@code role}.
     *
     * @param role role of the the user that has been successfully registered
     * @pre {@code (role.equals("Card Collector") ^ role.equals("Card Creator"))}
     * @throws NullPointerException if {@code role == null}
     * @throws IllegalArgumentException if {@code !((role.equals("Card Collector") ^ role.equals("Card Creator"))))}
     * @post registration-success callback function has been called
     */
    void registrationSuccess(String role) throws NullPointerException, IllegalArgumentException;

    /**
     * Propagates registration-failure signals.
     *
     * @post database-registration-failure function has been called
     */
    void registrationDatabaseFail();
}
