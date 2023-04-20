package com.example.cardhub.TradingMode;

import java.util.List;
import java.util.Set;

/**
 * Binds a TradeModeState to a TradingSessionRepository
 */
public interface TradingSessionRepositoryReceiver {
    /**
     * Retrieve a method call request from the server to cancel the current trading session. This
     * method may be called when another client instance requests the trading session to be cancelled.
     */
    void cancelTradingSession();

    /**
     * Retrieve a response from the server after calling {@code cancelTradingSession(int clientID)},
     * as a confirmation that the current trading session should be cancelled.
     */
    void cancelTradingSessionResponse();


    /**
     * Retrieve a response from the server after calling {@code acceptProposedTrade(int clientID)},
     * together with information on whether the other client instance also accepted the trade.
     *
     * @param tradeAccepted whether the trade was accepted by the other client instance.
     */
    void acceptProposedTradeResponse(boolean tradeAccepted);

    /**
     * Retrieve a method call request from the server to show that the other user is trying to accept
     * the current trade proposal. This method should be called on the other app instance
     * immediately after the server receives {@code acceptTrade(int clientID)}.
     */
    void acceptProposedTradeFromOtherTrader();


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
     * Retrieve a response from the server after calling {@code changeProposedCards(int ClientID,
     * Set<CardDiff> diffs)}, as a confirmation that the changed proposed cards were handled correctly.
     */
    void changeProposedCardsResponse();

    /**
     * Start the trade timer.
     */
    void startTradeTimer();

    /**
     * Finish the trade.
     */
    void finishTrade();
}
