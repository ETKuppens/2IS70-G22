package com.example.cardhub.inventory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CardTest {

    private Card card;
    private String name = "Test Card";
    private String description = "This is a test card";
    private Card.Rarity rarity = Card.Rarity.COMMON;
    private String imageUrl = "https://test.card/image.jpg";

    @Before
    public void setUp() {
        card = new Card(name, description, rarity, imageUrl);
    }

    @Test
    public void testGetName() {
        assertEquals(name, card.NAME);
    }

    @Test
    public void testGetDescription() {
        assertEquals(description, card.DESCRIPTION);
    }

    @Test
    public void testGetRarity() {
        assertEquals(rarity, card.RARITY);
    }

    @Test
    public void testGetImageUrl() {
        assertEquals(imageUrl, card.IMAGE_URL);
    }

}
