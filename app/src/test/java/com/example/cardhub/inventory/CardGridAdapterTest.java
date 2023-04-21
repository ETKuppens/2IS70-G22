package com.example.cardhub.inventory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.test.core.app.ApplicationProvider;

import com.example.cardhub.R;
import com.example.cardhub.inventory.Card;
import com.example.cardhub.inventory.CardGridAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class CardGridAdapterTest {

    private CardGridAdapter cardGridAdapter;

    private List<Card> testCards;

    @Mock
    private LayoutInflater mockInflater;

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