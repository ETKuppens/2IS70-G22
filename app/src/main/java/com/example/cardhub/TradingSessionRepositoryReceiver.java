package com.example.cardhub;

/**
 * Binds a TradeModeState to a TradingSessionRepository
 */
public interface TradingSessionRepositoryReceiver {
    /**
     * Request the server to cancel the trading session.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has requested the trading session to be cancelled.
     */
    void cancelTradingSession(int clientID);

    /**
     * Retrieve a response from the server after calling {@code cancelTradingSession(int clientID)},
     * as a confirmation that the current trading session should be cancelled.
     */
    void cancelTradingSessionResponse();


    
}
