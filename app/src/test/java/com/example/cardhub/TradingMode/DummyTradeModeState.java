package com.example.cardhub.TradingMode;

import com.example.cardhub.inventory.Card;

import org.junit.Before;

import java.util.Set;

import kotlin.OverloadResolutionByLambdaReturnType;

public class DummyTradeModeState extends TradeModeState {
    /**
     * Construct a new TradeModeState that is linked to an existing TradeModeActivity.
     *
     * @param activity the TradeModeActivity storing the UI that should be represented by this TradeModeState.
     * @param repo
     * @param clientid ID of the client that instantiated this TradeModeState.
     */
    public DummyTradeModeState(TradeModeActivity activity, TradingSessionRepository repo, String clientid) {
        super(activity, repo, clientid);
    }


    private boolean changeProposedCardsFromUIWasCalled = false;

    @Override
    public void changeProposedCardsFromUI(Set<CardDiff> diffs) {
        this.changeProposedCardsFromUIWasCalled = true;
    }

    public boolean getChangeProposedCardsFromUIWasCalled() {
        return this.changeProposedCardsFromUIWasCalled;
    }


    private boolean cancelTradingSessionFromUIWasCalled = false;

    @Override
    public void cancelTradingSessionFromUI() {
        this.cancelTradingSessionFromUIWasCalled = true;
    }

    public boolean getCancelTradingSessionFromUIWasCalled() {
        return this.cancelTradingSessionFromUIWasCalled;
    }


    private boolean backPressedFromUIWasCalled = false;

    @Override
    public void backPressedFromUI() {
        this.backPressedFromUIWasCalled = true;
    }

    public boolean getBackPressedFromUIWasCalled() {
        return this.backPressedFromUIWasCalled;
    }


    private boolean readyFromUIWasCalled = false;

    @Override
    public void readyFromUI() {
        this.readyFromUIWasCalled = true;
    }

    public boolean getReadyFromUIWasCalled() {
        return this.readyFromUIWasCalled;
    }


    private boolean cardSelectFromUIWasCalled = false;

    @Override
    public void cardSelectFromUI() {
        this.cardSelectFromUIWasCalled = true;
    }

    public boolean getCardSelectFromUIWasCalled() {
        return this.cardSelectFromUIWasCalled;
    }


    private boolean proposedCardClickedFromUIWasCalled = false;

    @Override
    public void proposedCardClickedFromUI(Card clickedCard) {
        this.proposedCardClickedFromUIWasCalled = true;
    }

    public boolean getProposedCardClickedFromUIWasCalled() {
        return this.proposedCardClickedFromUIWasCalled;
    }


    private boolean cancelTradingSessionWasCalled = false;

    @Override
    public void cancelTradingSession() {
        this.cancelTradingSessionWasCalled = true;
    }

    public boolean getCancelTradingSessionWasCalled() {
        return this.cancelTradingSessionWasCalled;
    }


    private boolean cancelTradingSessionResponseWasCalled = false;

    @Override
    public void cancelTradingSessionResponse() {
        this.cancelTradingSessionResponseWasCalled = true;
    }

    public boolean getCancelTradingSessionResponseWasCalled() {
        return this.cancelTradingSessionResponseWasCalled;
    }


    private boolean acceptProposedTradeResponseWasCalled = false;

    @Override
    public void acceptProposedTradeResponse(boolean tradeAccepted) {
        this.acceptProposedTradeResponseWasCalled = true;
    }

    public boolean getAcceptProposedTradeResponseWasCalled() {
        return acceptProposedTradeResponseWasCalled;
    }


    private boolean acceptProposedTradeFromOtherTraderWasCalled = false;

    @Override
    public void acceptProposedTradeFromOtherTrader() {
        this.acceptProposedTradeFromOtherTraderWasCalled = true;
    }

    public boolean getAcceptProposedTradeFromOtherTraderWasCalled() {
        return this.acceptProposedTradeFromOtherTraderWasCalled;
    }


    private boolean changeProposedCardsWasCalled = false;
    private Set<CardDiff> diffs = null;

    @Override
    public void changeProposedCards(Set<CardDiff> diffs) {
        this.changeProposedCardsWasCalled = true;
        this.diffs = diffs;
    }

    public boolean getChangeProposedCardsWasCalled() {
        return this.changeProposedCardsWasCalled;
    }

    public Set<CardDiff> getDiffsAfterChangeProposedCards() {
        return this.diffs;
    }


    private boolean changeProposedCardsResponseWasCalled = false;

    @Override
    public void changeProposedCardsResponse() {
        this.changeProposedCardsResponseWasCalled = true;
    }

    public boolean getChangeProposedCardsResponseWasCalled() {
        return this.changeProposedCardsResponseWasCalled;
    }


    private boolean startTradeTimerWasCalled = false;

    @Override
    public void startTradeTimer() {
        this.startTradeTimerWasCalled = true;
    }

    public boolean getStartTradeTimerWasCalled() {
        return this.startTradeTimerWasCalled;
    }


    private boolean finishTradeWasCalled = false;

    @Override
    public void finishTrade() {
        this.finishTradeWasCalled = true;
    }

    public boolean getFinishTradeWasCalled() {
        return this.finishTradeWasCalled;
    }


    private boolean getCardMayBeRemovedWasCalled = false;

    @Override
    public boolean getCardMayBeRemoved() {
        this.getCardMayBeRemovedWasCalled = true;
        return false;
    }

    public boolean getGetCardMayBeRemovedWasCalled() {
        return this.getCardMayBeRemovedWasCalled;
    }
}
