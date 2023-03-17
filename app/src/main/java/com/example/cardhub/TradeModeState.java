package com.example.cardhub;

import java.util.Set;

public class TradeModeState implements TradingSessionRepositoryReceiver {
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

    /**
     * Update the UI in activity using the data from the TradingSession instance.
     */
    private void updateUI() {
        activity.updateUI(tradingSession);
    }

    @Override
    public void cancelTradingSession(int clientID){

    }

    @Override
    public void cancelTradingSession() {

    }

    @Override
    public void cancelTradingSessionConfirm(int clientID) {

    }

    @Override
    public void cancelTradingSessionResponse() {

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

    }

    @Override
    public void changeProposedCards(Set<CardDiff> diffs) {

    }

    @Override
    public void changeProposedCardsConfirm(int clientID) {

    }

    @Override
    public void changeProposedCardsResponse() {

    }
}
