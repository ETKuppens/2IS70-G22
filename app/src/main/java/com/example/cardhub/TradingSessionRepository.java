package com.example.cardhub;

import java.util.Set;

/**
 * Interface for classes implementing the TradingSessionRepo functionality;
 * be the link between a TradeModeState and a TradingSessionData.
 */
public interface TradingSessionRepository {
    /**
     * Request the server to cancel the trading session.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has requested the trading session to be cancelled.
     */
    void cancelTradingSession(int clientID);

    /**
     * Send a confirmation message to the server that the client has cancelled the trading session
     * correctly.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has cancelled their trading session correctly.
     */
    void cancelTradingSessionConfirm(int clientID);


    /**
     * Request the server to accept the currently proposed trade.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has requested to accept the proposed trade.
     */
    void acceptProposedTrade(int clientID);

    /**
     * Send a confirmation message to the server that the current trade acceptance has been
     * canceled correctly.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has requested to cancel the trade accept request.
     */
    void cancelAcceptTrade(int clientID);


    /**
     * Request the server to update the other client with a difference in proposed cards.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has requested to update their proposed cards.
     * @param diffs a set of CardDiffs that should be applied to the other clients' instance of
     * TradingSession.
     */
    void changeProposedCards(int clientID, Set<CardDiff> diffs);

    /**
     * Send a confirmation message to the server that the client has changed the proposed cards
     * correctly.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has just changed their TradingSession data.
     */
    void changeProposedCardsConfirm(int clientID);
}
