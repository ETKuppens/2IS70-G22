package com.example.cardhub.TradingMode;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashSet;

@RunWith(RobolectricTestRunner.class)
public class TradeModeStateTest {
    private DummyTradeModeActivity activity = null;
    private TradeModeState state = null;
    private DummyTradingSessionRepository repository = null;

    @Before
    public void setupClasses() {
        activity = new DummyTradeModeActivity();
        repository = new DummyTradingSessionRepository();
        state = getNewState(repository);
    }

    @Test
    public void cancelTradingSessionFromUI() {
        state.cancelTradingSessionFromUI();

        assertTrue(activity.getDisableChangeTradeProposalWasCalled());
        assertTrue(activity.getDisableAcceptTradeWasCalled());
        assertTrue(activity.getDisableCancelTradeWasCalled());
        assertTrue(activity.getCancelTradeModeWasCalled());

        assertThrows(RuntimeException.class, () -> state.cancelTradingSessionFromUI());
    }

    @Test
    public void backPressedFromUI() {
        state.backPressedFromUI();

        assertTrue(activity.getDisableChangeTradeProposalWasCalled());
        assertTrue(activity.getDisableAcceptTradeWasCalled());
        assertTrue(activity.getDisableCancelTradeWasCalled());
        assertTrue(activity.getCancelTradeModeWasCalled());
    }

    @Test
    public void readyFromUI() {
        state.readyFromUI();

        assertTrue(activity.getDisableAcceptTradeWasCalled());
        assertTrue(repository.getAcceptProposedTradeWasCalled());

        assertThrows(RuntimeException.class, () -> state.readyFromUI());
    }

    @Test
    public void proposedCardClickedFromUI() {
        assertThrows(RuntimeException.class,
                () -> state.proposedCardClickedFromUI(null));
    }

    @Test
    public void cancelTradingSession() {
        state.cancelTradingSession();

        assertTrue(activity.getDisableChangeTradeProposalWasCalled());
        assertTrue(activity.getDisableAcceptTradeWasCalled());
        assertTrue(repository.getCancelTradingSessionConfirmWasCalled());
        assertTrue(activity.getCancelTradeModeWasCalled());
    }

    @Test
    public void cancelTradingSessionResponse() {
        state.cancelTradingSessionResponse();

        assertTrue(activity.getDisableAcceptTradeWasCalled());
        assertTrue(activity.getCancelTradeModeWasCalled());
    }

    @Test
    public void acceptProposedTradeResponseFalse() {
        state.acceptProposedTradeResponse(false);

        assertTrue(activity.getEnableChangeTradeProposalWasCalled());
        assertTrue(activity.getEnableAcceptTradeWasCalled());
        assertTrue(activity.getEnableCancelTradeWasCalled());
        assertTrue(repository.getCancelAcceptTradeWasCalled());
    }

    @Test
    public void acceptProposedTradeResponseTrue() {
        state.acceptProposedTradeResponse(true);

        assertTrue(activity.getDisableAcceptTradeWasCalled());
        assertTrue(repository.getAcceptProposedTradeWasCalled());

        assertThrows(RuntimeException.class, () -> state.acceptProposedTradeResponse(true));
    }

    @Test
    public void acceptProposedTradeFromOtherTrader() {
        state.acceptProposedTradeFromOtherTrader();

        assertTrue(activity.getOtherPlayerReadyMessageIsVisible());
    }

    @Test
    public void changeProposedCards() {
        state.changeProposedCards(new HashSet<CardDiff>());

        assertTrue(repository.getChangeProposedCardsConfirmWasCalled());
    }

    @Test
    public void changeProposedCardsResponse() {
        state.changeProposedCardsResponse();

        assertTrue(activity.getEnableChangeTradeProposalWasCalled());
    }

    @Test
    public void finishTrade() {
        state.finishTrade();

        assertTrue(activity.getFinishTradeWasCalled());
    }

    @Test
    public void getCardMayBeRemovedInitial() {
        assertTrue(state.getCardMayBeRemoved());
    }

    @Test
    public void getCardMayBeRemovedAfterCancelTradingSessionFromUI() {
        state.cancelTradingSessionFromUI();

        assertFalse(state.getCardMayBeRemoved());
    }


    private TradeModeState getNewState(TradingSessionRepository repo) {
        return new TradeModeState(activity, repo, "unit@test.com");
    }
}