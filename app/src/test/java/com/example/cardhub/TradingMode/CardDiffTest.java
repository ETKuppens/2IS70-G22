package com.example.cardhub.TradingMode;

import static org.junit.Assert.*;

import com.example.cardhub.inventory.Card;

import org.junit.Test;

import java.util.Map;

public class CardDiffTest {

    Card testCard = new Card("TestName", "TestDescription", Card.Rarity.COMMON, "TestImageURL");

    private CardDiff testCardDiffAdd = new CardDiff(testCard, CardDiff.DiffOption.ADD);
    private CardDiff testCardDiffRemove = new CardDiff(testCard, CardDiff.DiffOption.REMOVE);

    /**
     * Test the getCard method of CardDiff.
     */
    @Test
    public void getCard() {
        testGetCard(testCardDiffAdd, testCard);
        testGetCard(testCardDiffRemove, testCard);
    }

    /**
     * Test the getDiff method of CardDiff.
     */
    @Test
    public void getDiff() {
        testGetDiff(testCardDiffAdd, CardDiff.DiffOption.ADD);
        testGetDiff(testCardDiffRemove, CardDiff.DiffOption.REMOVE);
    }

    /**
     * Test the serialize method of CardDiff.
     */
    @Test
    public void serialize() {
        testSerialize(testCardDiffAdd, testCard, CardDiff.DiffOption.ADD);
        testSerialize(testCardDiffRemove, testCard, CardDiff.DiffOption.REMOVE);
    }

    private void testGetCard(CardDiff cardDiffToTest, Card expectedCard) {
        assertEquals(cardDiffToTest.getCard(), expectedCard);
    }

    private void testGetDiff(CardDiff cardDiffToTest, CardDiff.DiffOption expectedDiffOption) {
        assertEquals(cardDiffToTest.getDiff(), expectedDiffOption);
    }

    private void testSerialize(CardDiff cardDiffToTest,
                               Card expectedCard,
                               CardDiff.DiffOption expectedDiffOption) {
        Map<String, Object> serialization = cardDiffToTest.serialize();

        @SuppressWarnings("unchecked")
        Map<String, Object> trueCardSerialized = (Map<String, Object>)serialization.get("card");
        CardDiff.DiffOption trueDiffOption = (CardDiff.DiffOption)serialization.get("diff");

        assertEquals(expectedCard.DESCRIPTION, (String)trueCardSerialized.get("description"));
        assertEquals(expectedCard.IMAGE_URL, (String)trueCardSerialized.get("imageurl"));
        assertEquals(expectedCard.NAME, (String)trueCardSerialized.get("name"));
        assertEquals(expectedCard.RARITY, (Card.Rarity)trueCardSerialized.get("rarity"));

        assertEquals(expectedDiffOption, trueDiffOption);
    }
}
