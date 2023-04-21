package com.example.cardhub.inventory;

import com.example.cardhub.inventory.Card;
import com.example.cardhub.inventory.CardSorter;
import com.example.cardhub.inventory.CardSorter.SortAttribute;
import com.example.cardhub.inventory.CardSorter.SortOrder;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CardSorterTest {
    @Test
    public void testSortOnNameDefaultOrder() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("Card A", Card.Rarity.COMMON, "fake_image_address.jpg"));
        cards.add(new Card("Card B", Card.Rarity.RARE, "fake_image_address.jpg"));
        cards.add(new Card("Card C", Card.Rarity.ULTRA_RARE, "fake_image_address.jpg"));
        cards.add(new Card("Card D", Card.Rarity.LEGENDARY, "fake_image_address.jpg")); // lower case name
        CardSorter.SortOnNameDefaultOrder(cards);

        assertEquals("Card A", cards.get(0).NAME);
        assertEquals("Card B", cards.get(1).NAME);
        assertEquals("Card C", cards.get(2).NAME);
        assertEquals("Card D", cards.get(3).NAME);
    }

    @Test
    public void testSortOnNameReverseOrder() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("Card A", Card.Rarity.COMMON, "fake_image_address.jpg"));
        cards.add(new Card("Card B", Card.Rarity.RARE, "fake_image_address.jpg"));
        cards.add(new Card("Card C", Card.Rarity.ULTRA_RARE, "fake_image_address.jpg"));
        cards.add(new Card("Card D", Card.Rarity.LEGENDARY, "fake_image_address.jpg")); // lower case name
        CardSorter.SortOnNameReverseOrder(cards);

        assertEquals("Card D", cards.get(0).NAME);
        assertEquals("Card C", cards.get(1).NAME);
        assertEquals("Card B", cards.get(2).NAME);
        assertEquals("Card A", cards.get(3).NAME);
    }

    @Test
    public void testSortOnRarityDefaultOrder() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("Card A", Card.Rarity.COMMON, "fake_image_address.jpg"));
        cards.add(new Card("Card B", Card.Rarity.RARE, "fake_image_address.jpg"));
        cards.add(new Card("Card C", Card.Rarity.ULTRA_RARE, "fake_image_address.jpg"));
        cards.add(new Card("Card D", Card.Rarity.COMMON, "fake_image_address.jpg"));
        cards.add(new Card("Card E", Card.Rarity.RARE, "fake_image_address.jpg"));
        cards.add(new Card("Card F", Card.Rarity.LEGENDARY, "fake_image_address.jpg"));
        CardSorter.SortOnRarityDefaultOrder(cards);

        assertEquals(Card.Rarity.ULTRA_RARE, cards.get(0).RARITY);
        assertEquals(Card.Rarity.COMMON, cards.get(1).RARITY);
        assertEquals(Card.Rarity.COMMON, cards.get(2).RARITY);
        assertEquals(Card.Rarity.RARE, cards.get(3).RARITY);
        assertEquals(Card.Rarity.RARE, cards.get(4).RARITY);
        assertEquals(Card.Rarity.LEGENDARY, cards.get(5).RARITY);
    }

}