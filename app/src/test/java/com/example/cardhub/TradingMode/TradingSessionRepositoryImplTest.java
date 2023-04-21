package com.example.cardhub.TradingMode;

import static org.junit.Assert.*;

import com.example.cardhub.inventory.Card;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(RobolectricTestRunner.class)
public class TradingSessionRepositoryImplTest {

    /**
     * Instance of a dummy TradingSessionData used to check whether the data methods are called
     * correctly by TradingSessionRepositoryImpl.
     */
    private DummyTradingSessionData data = null;

    /**
     * Instance of a dummy TradeModeActivity used to construct the state instance correctly.
     */
    private DummyTradeModeActivity activity = null;

    /**
     * Instance of a dummy TradeModeState used to check whether the state methods are called
     * correctly by TradingSessionRepositoryImpl.
     */
    private DummyTradeModeState state = null;

    /**
     * Instance of a TradingSessionRepositoryImpl used to check whether its functionality is
     * correct.
     */
    private TradingSessionRepositoryImpl repository = null;

    /**
     * ID of the client, used for constructing the state.
     */
    private final String CLIENT_ID = "unit@test.com";

    @Before
    public void setupClasses() {
        repository = new TradingSessionRepositoryImpl();

        activity = new DummyTradeModeActivity();
        state = new DummyTradeModeState(activity, repository, CLIENT_ID);

        data = new DummyTradingSessionData();
        repository.setData(data);
        repository.setReceiver(state);
    }

    @Test
    public void cancelTradingSession() {
        repository.cancelTradingSession(CLIENT_ID);

        assertTrue(data.getCancelTradingSessionWasCalled());
    }

    @Test
    public void acceptProposedTrade() {
        repository.acceptProposedTrade(CLIENT_ID);

        assertTrue(data.getAcceptProposedTradeWasCalled());
    }

    @Test
    public void cancelAcceptTrade() {
        repository.cancelAcceptTrade(CLIENT_ID);

        assertTrue(data.getCancelAcceptTradeWasCalled());
    }

    @Test
    public void changeProposedCards() {
        repository.changeProposedCards(CLIENT_ID, null);

        assertTrue(data.getChangeProposedCardsWasCalled());
    }

    @Test
    public void changeProposedCardsConfirm() {
        repository.changeProposedCardsConfirm(CLIENT_ID);

        assertTrue(state.getChangeProposedCardsResponseWasCalled());
    }

    @Test
    public void receiveUpdate() {
        Card card = new Card("cardName",
                              "cardDescription",
                               Card.Rarity.COMMON,
                              "cardURL");

        Map<String, Object> cardDiffMap = new HashMap<String, Object>();
        cardDiffMap.put("card", card.serialize());
        cardDiffMap.put("diff", CardDiff.DiffOption.ADD.toString());

        List<Map<String, Object>> diffList = Arrays.asList(cardDiffMap);

        repository.receiveUpdate(diffList);
        Set<CardDiff> decodedDiffs = state.getDiffsAfterChangeProposedCards();

        assertTrue(state.getChangeProposedCardsWasCalled());
        assertTrue(decodedDiffs.size() == 1);

        for (CardDiff diff : decodedDiffs) {
            assertEquals(CardDiff.DiffOption.ADD, diff.getDiff());
            assertEquals("cardName", diff.getCard().NAME);
            assertEquals("cardDescription", diff.getCard().DESCRIPTION);
            assertEquals(Card.Rarity.COMMON, diff.getCard().RARITY);
            assertEquals("cardURL", diff.getCard().IMAGE_URL);
        }
    }

    @Test
    public void startTradeTimer() {
        repository.startTradeTimer();

        assertTrue(state.getStartTradeTimerWasCalled());
    }

    @Test
    public void doTrade() {
        repository.doTrade();

        assertTrue(data.getDoTradeWasCalled());
    }

    @Test
    public void finishTrade() {
        repository.finishTrade();

        assertTrue(state.getFinishTradeWasCalled());
    }
}