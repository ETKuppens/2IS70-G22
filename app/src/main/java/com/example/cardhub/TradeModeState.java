package com.example.cardhub;

import java.util.Set;

public class TradeModeState implements TradingSessionRepositoryReceiver {
    int clientID = 0;

    TradeModeActivity activity;

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


    /**
     * Update the UI in activity using the data from the TradingSession instance.
     */
    private void updateUI() {
        activity.updateUI(tradingSession);
    }

    private void cancelTradeMode() {
        this.activity.cancelTradeMode();
    }

    @Override
    public void cancelTradingSession(int clientID){

    }

    @Override
    public void cancelTradingSession() {
        cancelTradingSessionConfirm(this.clientID);
        cancelTradeMode();
    }

    @Override
    public void cancelTradingSessionConfirm(int clientID) {

    }

    @Override
    public void cancelTradingSessionResponse() {
        cancelTradeMode();
    }


    @Override
    public void acceptProposedTrade(int clientID) {

    }

    @Override
    public void acceptProposedTradeResponse(boolean tradeAccepted) {

    }

    @Override
    public void cancelAcceptTrade(int clientID) {

    }


    @Override
    public void changeProposedCards(int clientID, Set<CardDiff> diffs) {
        if (!this.proposedCardsMayBeChanged)
        {
            throw new RuntimeException("TradeModeState.changeProposedCards: the trade session is" +
                    "currently in a state where the proposed cards may not be changed.");
        }

        this.proposedCardsMayBeChanged = false;
        this.activity.disableChangeTradeProposal();
    }

    @Override
    public void changeProposedCards(Set<CardDiff> diffs) {
        this.tradingSession.AddCardDiffsForOtherUser(diffs);
        updateUI();
        changeProposedCardsConfirm(this.clientID);
    }

    @Override
    public void changeProposedCardsConfirm(int clientID) {

    }

    @Override
    public void changeProposedCardsResponse() {
        this.proposedCardsMayBeChanged = true;
        this.activity.enableChangeTradeProposal();
    }
}
