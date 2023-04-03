package com.example.cardhub.TradingMode;

import android.widget.Toast;

import com.example.cardhub.TradeModeActivity;

import java.util.Set;

public class TradeModeState implements TradingSessionRepositoryReceiver {
    private int clientID = 0;

    private TradeModeActivity activity;
    private TradingSessionRepository repository;

    // TradingSession instance that keeps track which user proposes which cards in the current
    // trading session.
    private TradingSession tradingSession = new TradingSession();

    /**
     * Construct a new TradeModeState that is linked to an existing TradeModeActivity.
     *
     * @param activity the TradeModeActivity storing the UI that should be represented by this TradeModeState.
     * @param lid lobby id of the trade.
     */
    public TradeModeState(TradeModeActivity activity, String lid) {
        this.activity = activity;
        this.repository = new TradingSessionRepositoryImpl(lid);
    }

    // List of flags that are used to check when certain functionality can be called.
    /**
     * Whether the cards that this user proposes in the trade may be changed. Must be false when
     * this app instance is currently waiting for a response from the server stating that a previous
     * proposed cards changed was handled correctly.
     */
    private boolean proposedCardsMayBeChanged = true;
    /**
     * Whether the current trading proposal may be accepted by this app instance. May be false when
     * this app instance is currently canceling the trading session, or when this app instance is
     * already proposing the trade to be accepted.
     */
    private boolean proposedTradeMayBeAccepted = true;
    /**
     * Whether the current trading proposal may be canceled by this app instance. May be false when
     * this app instance is currently trying to accept the trading proposal.
     */
    private boolean proposedTradeMayBeCanceled = true;

    public void changeProposedCardsFromUI(Set<CardDiff> diffs) {
        if (!this.proposedCardsMayBeChanged)
        {
            throw new RuntimeException("TradeModeState.changeProposedCards: the trade session is" +
                    "currently in a state where the proposed cards may not be changed.");
        }

        this.proposedCardsMayBeChanged = false;
        this.activity.disableChangeTradeProposal();

        this.tradingSession.AddCardDiffsForThisUser(diffs);
        updateUI();

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
        if (!this.proposedTradeMayBeCanceled) {
            throw new RuntimeException("TradeModeState.cancelTradingSessionFromUI: the trade " +
                    "session is" + "currently in a state where the proposed trade may not be " +
                    "canceled.");
        }

        this.proposedCardsMayBeChanged = false;
        this.activity.disableChangeTradeProposal();

        this.proposedTradeMayBeAccepted = false;
        this.activity.disableAcceptTrade();

        this.proposedTradeMayBeCanceled = false;
        this.activity.disableCancelTrade();

        this.repository.cancelAcceptTrade(this.clientID);
        cancelTradeMode();
    }

    public void backPressedFromUI() {
        if (this.proposedTradeMayBeCanceled) {
            this.cancelTradingSessionFromUI();
        } else {
            this.activity.showCancelByBackPressedToast();
        }
    }

    /**
     * Receive a message that the ready button was clicked in the UI.
     */
    public void readyFromUI() {
        acceptTrade();
    }

    /**
     * Update the UI in activity using the data from the TradingSession instance.
     */
    private void updateUI() {
        activity.updateUI(tradingSession);
    }

    private void cancelTradeMode() {
        this.proposedTradeMayBeAccepted = false;
        this.activity.disableAcceptTrade();

        this.activity.cancelTradeMode();
    }

    /**
     * Apply the proposed cards to the inventory of this user, and exit this trading session.
     */
    private void acceptTrade() {
        if (!this.proposedTradeMayBeAccepted) {
            throw new RuntimeException("TradeModeState.acceptTrade: the trade session is" +
                    "currently in a state where the proposed trade may not be accepted.");
        }

        this.proposedCardsMayBeChanged = false;
        this.activity.disableChangeTradeProposal();

        this.proposedTradeMayBeAccepted = false;
        this.activity.disableAcceptTrade();

        this.proposedTradeMayBeCanceled = false;
        this.activity.disableCancelTrade();

        this.repository.acceptProposedTrade(this.clientID);
    }


    @Override
    public void cancelTradingSession() {
        this.proposedCardsMayBeChanged = false;
        this.activity.disableChangeTradeProposal();

        this.proposedTradeMayBeAccepted = false;
        this.activity.disableAcceptTrade();

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

            this.proposedTradeMayBeAccepted = true;
            this.activity.enableAcceptTrade();

            this.proposedTradeMayBeCanceled = true;
            this.activity.enableCancelTrade();

            this.repository.cancelAcceptTrade(this.clientID);
        }
    }

    @Override
    public void acceptProposedTradeFromOtherTrader() {
        this.activity.enableOtherPlayerReadyMessage();
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

    public boolean getCardMayBeRemoved() {
        if (this.proposedCardsMayBeChanged) {
            return true;
        }

        Toast toast = Toast.makeText(activity,
                "Cannot currently change proposed cards",
                Toast.LENGTH_LONG);
        toast.show();

        return false;
    }
}
