package com.example.cardhub.map;

import static org.junit.Assert.*;

import com.example.cardhub.inventory.Card;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(RobolectricTestRunner.class)
public class MapStateTest {

    DummyMapRepository repository = null;
    DummyMapActivity activity = null;
    MapState state = null; // State to test methods on.


    @Before
    public void SetupClasses() {
        activity = new DummyMapActivity();
        repository = new DummyMapRepository();
        state = new MapState(activity, repository);
    }

    @Test
    public void requestPacks() {
        state.requestPacks();

        assertTrue(repository.getRequestPacksWasCalled());
    }

    @Test
    public void setPacks() {
        List<CardPack> setList = new ArrayList<>();
        state.setPacks(setList);

        assertTrue(activity.getCardsResponseWasCalled());
        assertEquals(setList, activity.getPacksGivenAtCardsResponse());
    }

    @Test
    public void acquireRandomCardCommon() {
        state.acquireRandomCard(Card.Rarity.COMMON);

        assertTrue(repository.getAcquireRandomCardWasCalled());
        assertEquals(Card.Rarity.COMMON, repository.getRarityGivenAtAcquireRandomCard());
    }

    @Test
    public void acquireRandomCardLegendary() {
        state.acquireRandomCard(Card.Rarity.LEGENDARY);

        assertTrue(repository.getAcquireRandomCardWasCalled());
        assertEquals(Card.Rarity.LEGENDARY, repository.getRarityGivenAtAcquireRandomCard());
    }

    @Test
    public void acquireRandomCardCallbackNull() {
        state.acquireRandomCardCallback(null);

        assertTrue(activity.getShowCardpackPreviewWindowWasCalled());

        List<Card> collectedCardPack = activity.getCardPackCardsGivenAtShowCardpackPreviewWindow();
        assertEquals(1, collectedCardPack.size());

        for (Card card : collectedCardPack) {
            assertEquals(null, card);
        }
    }

    @Test
    public void acquireRandomCardCallbackValidCard() {
        Card testCard = new Card("CardName",
                "CardDescription",
                Card.Rarity.ULTRA_RARE,
                "CardImageURL");

        state.acquireRandomCardCallback(null);

        assertTrue(activity.getShowCardpackPreviewWindowWasCalled());

        List<Card> collectedCardPack = activity.getCardPackCardsGivenAtShowCardpackPreviewWindow();
        assertEquals(1, collectedCardPack.size());

        for (Card card : collectedCardPack) {
            assertEquals(null, card);
        }
    }
}