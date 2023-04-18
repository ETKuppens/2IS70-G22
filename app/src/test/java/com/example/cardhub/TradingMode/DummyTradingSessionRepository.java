package com.example.cardhub.TradingMode;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DummyTradingSessionRepository implements TradingSessionRepository {
    boolean setReceiverWasCalled = false;

    /**
     * Set the receiver this TradingSessionRepository should be interacting with.
     * @param receiver the TradingSessionRepositoryReceiver that this TradingSessionRepository
     *                 should be interacting with.
     */
    @Override
    public void setReceiver(TradingSessionRepositoryReceiver receiver) {
        this.setReceiverWasCalled = true;
    }

    public boolean getSetReceiverWasCalled() {
        return this.setReceiverWasCalled;
    }


    boolean cancelTradingSessionWasCalled = false;

    /**
     * Request the server to cancel the trading session.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has requested the trading session to be cancelled.
     */
    public void cancelTradingSession(String clientID) {
        this.cancelTradingSessionWasCalled = true;
    }

    public boolean getCancelTradingSessionWasCalled() {
        return this.cancelTradingSessionWasCalled;
    }


    boolean cancelTradingSessionConfirmWasCalled = false;

    /**
     * Send a confirmation message to the server that the client has cancelled the trading session
     * correctly.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has cancelled their trading session correctly.
     */
    public void cancelTradingSessionConfirm(String clientID) {
        this.cancelTradingSessionConfirmWasCalled = true;
    }

    public boolean getCancelTradingSessionConfirmWasCalled() {
        return this.cancelTradingSessionConfirmWasCalled;
    }


    boolean acceptProposedTradeWasCalled = false;

    /**
     * Request the server to accept the currently proposed trade.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has requested to accept the proposed trade.
     */
    public void acceptProposedTrade(String clientID) {
        this.acceptProposedTradeWasCalled = true;
    }

    public boolean getAcceptProposedTradeWasCalled() {
        return this.acceptProposedTradeWasCalled;
    }


    boolean cancelAcceptTradeWasCalled = false;

    /**
     * Send a confirmation message to the server that the current trade acceptance has been
     * canceled correctly.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has requested to cancel the trade accept request.
     */
    public void cancelAcceptTrade(String clientID) {
        this.cancelAcceptTradeWasCalled = true;
    }

    public boolean getCancelAcceptTradeWasCalled() {
        return this.cancelAcceptTradeWasCalled;
    }


    boolean changeProposedCardsWasCalled = false;

    /**
     * Request the server to update the other client with a difference in proposed cards.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has requested to update their proposed cards.
     * @param diffs a set of CardDiffs that should be applied to the other clients' instance of
     * TradingSession.
     */
    public void changeProposedCards(String clientID, Set<CardDiff> diffs) {
        this.changeProposedCardsWasCalled = true;
    }

    public boolean getChangeProposedCardsWasCalled() {
        return this.changeProposedCardsWasCalled;
    }


    boolean changeProposedCardsConfirmWasCalled = false;

    /**
     * Send a confirmation message to the server that the client has changed the proposed cards
     * correctly.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     * identify which side of the trading session has just changed their TradingSession data.
     */
    public void changeProposedCardsConfirm(String clientID) {
        this.changeProposedCardsConfirmWasCalled = true;
    }

    public boolean getChangeProposedCardsConfirmWasCalled() {
        return this.changeProposedCardsConfirmWasCalled;
    }


    boolean receiveUpdateWasCalled = false;

    /**
     * Receive a message from the server containing a list of cardDiffs representing changes in
     * the current trading session.
     * @param diffs list of encoded cardDiffs.
     */
    public void receiveUpdate(List<Map<String, Object>> diffs) {
        this.receiveUpdateWasCalled = true;
    }

    public boolean getReceiveUpdateWasCalled() {
        return this.receiveUpdateWasCalled;
    }


    boolean startTradeTimerWasCalled = false;

    /**
     * Start the timer for the trade.
     */
    public void startTradeTimer() {
        this.startTradeTimerWasCalled = true;
    }

    public boolean getStartTradeTimerWasCalled() {
        return this.startTradeTimerWasCalled;
    }


    boolean doTradeWasCalled = false;

    /**
     * Apply the trade proposal.
     */
    public void doTrade() {
        this.doTradeWasCalled = true;
    }

    public boolean getDoTradeWasCalled() {
        return this.doTradeWasCalled;
    }


    boolean finishTradeWasCalled = false;

    /**
     * Finish the trade.
     */
    public void finishTrade() {
        this.finishTradeWasCalled = true;
    }

    public boolean getFinishTradeWasCalled() {
        return this.finishTradeWasCalled;
    }
}
