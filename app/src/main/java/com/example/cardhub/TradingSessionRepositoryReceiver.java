package com.example.cardhub;

import java.util.Set;

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
     * Retrieve a method call request from the server to cancel the current trading session. This
     * method may be called when another client instance requests the trading session to be cancelled.
     */
    void cancelTradingSession();

    /**
     * Send a confirmation message to the server that the client has cancelled the trading session
     * correctly.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has cancelled their trading session correctly.
     */
    void cancelTradingSessionConfirm(int clientID);

    /**
     * Retrieve a response from the server after calling {@code cancelTradingSession(int clientID)},
     * as a confirmation that the current trading session should be cancelled.
     */
    void cancelTradingSessionResponse();


    /**
     * Request the server to accept the currently proposed trade.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has requested to accept the proposed trade.
     */
    void acceptProposedTrade(int clientID);

    /**
     * Retrieve a response from the server after calling {@code acceptProposedTrade(int clientID)},
     * together with information on whether the other client instance also accepted the trade.
     *
     * @param tradeAccepted whether the trade was accepted by the other client instance.
     */
    void acceptProposedTradeResponse(boolean tradeAccepted);

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
     * Retrieve a method call request from the server to update the TradingSession, and to update
     * the UI. This method may be called when another client instance calls
     * {@code changeProposedCards(int clientID, Set<CardDiff> diffs)}.
     *
     * @param diffs a set of CardDiffs that should be applied to this clients' instance of
     * TradingSession.
     */
    void changeProposedCards(Set<CardDiff> diffs);

    /**
     * Send a confirmation message to the server that the client has changed the proposed cards
     * correctly.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has just changed their TradingSession data.
     */
    void changeProposedCardsConfirm(int clientID);

    /**
     * Retrieve a response from the server after calling {@code changeProposedCards(int ClientID,
     * Set<CardDiff> diffs)}, as a confirmation that the changed proposed cards were handled correctly.
     */
    void changeProposedCardsResponse();
}
