package com.example.cardhub.authentication;

/**
 * Interface for the State Design Pattern implementation of the password retrieval
 * for users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public interface ForgotPasswordReceiver {
    /**
     * Propagates send-reset-email-success signal.
     */
    public void sendForgotPasswordEmailSuccess();

    /**
     * Propagates send-reset-email-failure signal.
     */
    public void sendForgotPasswordEmailFailure();
}
