package com.example.cardhub.TradingMode;

import android.os.Bundle;

public class DummyTradeModeActivity extends TradeModeActivity {
    private boolean disableChangeTradeProposalWasCalled = false;

    /**
     * Get whether disableChangeTradeProposal was called.
     * @return disableChangeTradeProposalWasCalled
     */
    public boolean getDisableChangeTradeProposalWasCalled() {
        return this.disableChangeTradeProposalWasCalled;
    }

    @Override
    public void disableChangeTradeProposal() {
        disableChangeTradeProposalWasCalled = true;
    }


    private boolean disableAcceptTradeWasCalled = false;

    /**
     * Get whether disableAcceptTrade was called.
     * @return disableAcceptTradeWasCalled
     */
    public boolean getDisableAcceptTradeWasCalled() {
        return this.disableAcceptTradeWasCalled;
    }

    @Override
    public void disableAcceptTrade() {
        disableAcceptTradeWasCalled = true;
    }


    private boolean disableCancelTradeWasCalled = false;

    /**
     * Get whether disableCancelTrade was called.
     * @return disableCancelTradeWasCalled
     */
    public boolean getDisableCancelTradeWasCalled() {
        return this.disableCancelTradeWasCalled;
    }

    @Override
    public void disableCancelTrade() {
        disableCancelTradeWasCalled = true;
    }


    private boolean cancelTradeModeWasCalled = false;

    /**
     * Get whether cancelTradeMode was called.
     * @return cancelTradeModeWasCalled
     */
    public boolean getCancelTradeModeWasCalled() {
        return this.cancelTradeModeWasCalled;
    }

    @Override
    public void cancelTradeMode() {
        cancelTradeModeWasCalled = true;
    }


    private boolean enableChangeTradeProposalWasCalled = false;

    /**
     * Get whether enableChangeTradeProposal was called.
     * @return enableChangeTradeProposalWasCalled
     */
    public boolean getEnableChangeTradeProposalWasCalled() {
        return this.enableChangeTradeProposalWasCalled;
    }

    @Override
    public void enableChangeTradeProposal() {
        enableChangeTradeProposalWasCalled = true;
    }


    private boolean enableAcceptTradeWasCalled = false;

    /**
     * Get whether enableAcceptTrade was called.
     * @return enableAcceptTradeWasCalled
     */
    public boolean getEnableAcceptTradeWasCalled() {
        return this.enableAcceptTradeWasCalled;
    }

    @Override
    public void enableAcceptTrade() {
        enableAcceptTradeWasCalled = true;
    }


    private boolean enableCancelTradeWasCalled = false;

    /**
     * Get whether enableCancelTrade was called.
     * @return enableCancelTradeWasCalled
     */
    public boolean getEnableCancelTradeWasCalled() {
        return this.enableCancelTradeWasCalled;
    }

    @Override
    public void enableCancelTrade() {
        enableCancelTradeWasCalled = true;
    }


    private boolean otherPlayerReadyMessageIsVisible = false;

    /**
     * Get whether the other player ready message should be visible.
     * @return otherPlayerReadyMessageIsVisible
     */
    public boolean getOtherPlayerReadyMessageIsVisible() {
        return this.otherPlayerReadyMessageIsVisible;
    }

    @Override
    public void disableOtherPlayerReadyMessage() {
        otherPlayerReadyMessageIsVisible = false;
    }

    @Override
    public void enableOtherPlayerReadyMessage() {
        otherPlayerReadyMessageIsVisible = true;
    }


    private boolean finishTradeWasCalled = false;

    /**
     * Get whether finishTrade was called.
     * @return finishTradeWasCalled
     */
    public boolean getFinishTradeWasCalled() {
        return finishTradeWasCalled;
    }

    @Override
    public void finishTrade() {
        finishTradeWasCalled = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {}

    @Override
    public void updateUI(TradingSession tradingSession) {}

}
