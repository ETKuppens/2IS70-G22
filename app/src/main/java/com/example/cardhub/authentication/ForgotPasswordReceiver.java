package com.example.cardhub.authentication;

/**
 * Interface for the State Design Pattern implementation for sending password-reset-emails to users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public interface ForgotPasswordReceiver {
    /**
     * Propagates send-password-reset-mail-success signals.
     *
     * @post password-reset-mail-success callback function has been called
     */
    void sendForgotPasswordEmailSuccess();

    /**
     * Propagates send-password-reset-mail-database-failure signals.
     *
     * @post password-reset-mail-database-failure callback function has been called
     */
    void sendForgotPasswordEmailDatabaseFailure();
}
