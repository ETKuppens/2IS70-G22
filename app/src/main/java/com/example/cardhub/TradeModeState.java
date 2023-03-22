package com.example.cardhub;

import java.util.Set;

public class TradeModeState implements TradingSessionRepositoryReceiver {
    int clientID = 0;

    TradeModeActivity activity;
    TradingSessionRepository repository;

    // TradingSession instance that keeps track which user proposes which cards in the current
    // trading session.
    TradingSession tradingSession = new TradingSession();

    /**
     * Construct a new TradeModeState that is linked to an existing TradeModeActivity.
     * @param activity the TradeModeActivity storing the UI that should be represented by this TradeModeState.
     */
    TradeModeState(TradeModeActivity activity) {
        this.activity = activity;
    }

    // List of flags that are used to check when certain functionality can be called.
    /**
     * Whether the cards that this user proposes in the trade may be changed. Must be false when
     * this app instance is currently waiting for a response from the server stating that a previous
     * proposed cards changed was handled correctly.
     */
    boolean proposedCardsMayBeChanged = true;

    public void changeProposedCardsFromUI(Set<CardDiff> diffs) {
        if (!this.proposedCardsMayBeChanged)
        {
            throw new RuntimeException("TradeModeState.changeProposedCards: the trade session is" +
                    "currently in a state where the proposed cards may not be changed.");
        }

        this.proposedCardsMayBeChanged = false;
        this.activity.disableChangeTradeProposal();

        this.repository.changeProposedCards(this.clientID, diffs);
    }

    /**
     * Perform the steps needed to cancel the trading session;
     * 1. Make sure that the proposed cards cannot be changed anymore
     * 2. Ask the server to also cancel the trading mode of the other client instance in this
     * trading session
     * 3. Cancel the trading session of this client instance.
     */
    public void cancelTradingSessionFromUI() {
        this.proposedCardsMayBeChanged = false;
        this.activity.disableChangeTradeProposal();

        this.repository.cancelAcceptTrade(this.clientID);
        cancelTradeMode();
    }

    /**
     * Update the UI in activity using the data from the TradingSession instance.
     */
    private void updateUI() {
        activity.updateUI(tradingSession);
    }

    private void cancelTradeMode() {
        this.activity.cancelTradeMode();
    }

    /**
     * Apply the proposed cards to the inventory of this user, and exit this trading session.
     */
    private void acceptTrade() {

    }


    @Override
    public void cancelTradingSession() {
        this.proposedCardsMayBeChanged = false;
        this.activity.disableChangeTradeProposal();

        this.repository.cancelTradingSessionConfirm(this.clientID);
        cancelTradeMode();
    }


    @Override
    public void cancelTradingSessionResponse() {
        cancelTradeMode();
    }


    @Override
    public void acceptProposedTradeResponse(boolean tradeAccepted) {
        if (tradeAccepted) {
            acceptTrade();
        }
        else { // !tradeAccepted
            this.proposedCardsMayBeChanged = true;
            this.activity.enableChangeTradeProposal();

            this.repository.cancelAcceptTrade(this.clientID);
        }
    }

    @Override
    public void changeProposedCards(Set<CardDiff> diffs) {
        this.tradingSession.AddCardDiffsForOtherUser(diffs);
        updateUI();
        this.repository.changeProposedCardsConfirm(this.clientID);
    }

    @Override
    public void changeProposedCardsResponse() {
        this.proposedCardsMayBeChanged = true;
        this.activity.enableChangeTradeProposal();
    }
}
