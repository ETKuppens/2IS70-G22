package com.example.cardhub.inventory;

import static org.junit.Assert.assertEquals;
import android.content.Context;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;

public class CardGridAdapterTest {
    // Variables
    private CardGridAdapter cardGridAdapter;
    private List<Card> testCards;
    @Mock
    private Context mockContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testCards = new ArrayList<>();
        testCards.add(new Card("Test card1", Card.Rarity.COMMON, "fake_image_address.jpg"));
        testCards.add(new Card("Test card 2", Card.Rarity.LEGENDARY, "fake_image_address.jpg"));
        cardGridAdapter = new CardGridAdapter(mockContext, testCards);
    }

    @Test
    public void testGetCount() {
        assertEquals(testCards.size(), cardGridAdapter.getCount());
    }

    @Test
    public void testGetItem() {
        Card card = (Card) cardGridAdapter.getItem(0);
        assertEquals(testCards.get(0), card);
    }

    @Test
    public void testGetItemId() {
        long itemId = cardGridAdapter.getItemId(0);
        assertEquals(0, itemId);
    }

    @Test
    public void testUpdateData() {
        List<Card> newCards = new ArrayList<>();
        newCards.add(new Card("Test card1", Card.Rarity.COMMON, "fake_image_address.jpg"));
        newCards.add(new Card("Test card2", Card.Rarity.LEGENDARY, "fake_image_address.jpg"));
        cardGridAdapter.updateData(newCards);
        assertEquals(newCards.size(), cardGridAdapter.getCount());
        assertEquals(newCards.get(0), cardGridAdapter.getItem(0));
        assertEquals(newCards.get(1), cardGridAdapter.getItem(1));
    }

}