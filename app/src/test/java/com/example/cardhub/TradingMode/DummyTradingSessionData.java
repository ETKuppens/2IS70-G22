package com.example.cardhub.TradingMode;

import java.util.Set;

public class DummyTradingSessionData extends TradingSessionData {
    public DummyTradingSessionData() {
        super();
    }

    private boolean cancelTradingSessionWasCalled = false;

    @Override
    public void cancelTradingSession(String clientID) {
        cancelTradingSessionWasCalled = true;
    }

    public boolean getCancelTradingSessionWasCalled() {
        return this.cancelTradingSessionWasCalled;
    }


    private boolean acceptProposedTradeWasCalled = false;

    @Override
    public void acceptProposedTrade() {
        this.acceptProposedTradeWasCalled = true;
    }

    public boolean getAcceptProposedTradeWasCalled() {
        return this.acceptProposedTradeWasCalled;
    }


    private boolean cancelAcceptTradeWasCalled = false;

    @Override
    public void cancelAcceptTrade() {
        this.cancelAcceptTradeWasCalled = true;
    }

    public boolean getCancelAcceptTradeWasCalled() {
        return this.cancelAcceptTradeWasCalled;
    }


    private boolean changeProposedCardsWasCalled = false;

    @Override
    public void changeProposedCards(String clientID, Set<CardDiff> diffs) {
        this.changeProposedCardsWasCalled = true;
    }

    public boolean getChangeProposedCardsWasCalled() {
        return this.changeProposedCardsWasCalled;
    }

    private boolean doTradeWasCalled = false;

    @Override
    public void doTrade() {
        this.doTradeWasCalled = true;
    }

    public boolean getDoTradeWasCalled() {
        return this.doTradeWasCalled;
    }
}
