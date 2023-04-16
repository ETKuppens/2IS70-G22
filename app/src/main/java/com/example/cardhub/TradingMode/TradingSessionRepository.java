package com.example.cardhub.TradingMode;

import java.util.List;
import java.util.Map;
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
    void cancelTradingSession(String clientID);

    /**
     * Send a confirmation message to the server that the client has cancelled the trading session
     * correctly.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has cancelled their trading session correctly.
     */
    void cancelTradingSessionConfirm(String clientID);


    /**
     * Request the server to accept the currently proposed trade.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has requested to accept the proposed trade.
     */
    void acceptProposedTrade(String clientID);

    /**
     * Send a confirmation message to the server that the current trade acceptance has been
     * canceled correctly.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has requested to cancel the trade accept request.
     */
    void cancelAcceptTrade(String clientID);


    /**
     * Request the server to update the other client with a difference in proposed cards.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has requested to update their proposed cards.
     * @param diffs a set of CardDiffs that should be applied to the other clients' instance of
     * TradingSession.
     */
    void changeProposedCards(String clientID, Set<CardDiff> diffs);

    /**
     * Send a confirmation message to the server that the client has changed the proposed cards
     * correctly.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has just changed their TradingSession data.
     */
    void changeProposedCardsConfirm(String clientID);

    /**
     * Receive a message from the server containing a list of cardDiffs representing changes in
     * the current trading session.
     * @param diffs list of encoded cardDiffs.
     */
    void receiveUpdate(List<Map<String, Object>> diffs);

    /**
     * Start the timer for the trade.
     */
    void startTradeTimer();

    /**
     * Apply the trade proposal.
     */
    void doTrade();

    /**
     * Finish the trade.
     */
    void finishTrade();
}
