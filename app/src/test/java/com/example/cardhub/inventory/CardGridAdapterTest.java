package com.example.cardhub.inventory;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CardGridAdapterTest {

    Context context;
    /**
     * Generate a list of 8 cards.
     */
    private List<Card> getCards8() {
        String name = "c1";
        String description = "description1";
        Card.Rarity rarity = Card.Rarity.COMMON;
        String image = "https://en.ws-tcg.com/wp/wp-content/uploads/20210519181156/DAL_W79_E003_SAMPLE.png";
        Card c1 = new Card(name, description, rarity, image);

        name = "c2";
        description = "description2";
        rarity = Card.Rarity.COMMON;
        Card c2 = new Card(name, description, rarity, image);

        name = "duplicatename";
        description = "description3";
        rarity = Card.Rarity.COMMON;
        Card c3 = new Card(name, description, rarity, image);

        name = "duplicatename";
        description = "description4";
        rarity = Card.Rarity.RARE;
        Card c4 = new Card(name, description, rarity, image);

        name = "duplicatename";
        description = "description5";
        rarity = Card.Rarity.LEGENDARY;
        Card c5 = new Card(name, description, rarity, image);

        name = "differentnamelength";
        description = "description6";
        rarity = Card.Rarity.COMMON;
        Card c6 = new Card(name, description, rarity, image);

        name = "nameofevenadifferentlengthandrare";
        description = "description7";
        rarity = Card.Rarity.RARE;
        Card c7 = new Card(name, description, rarity, image);

        name = "anothernamelengthandlegendary";
        description = "description3";
        rarity = Card.Rarity.LEGENDARY;
        Card c8 = new Card(name, description, rarity, image);

        return Arrays.asList(c2, c1, c3, c4, c5, c6, c7, c8);
    }

    private List<Card> getCards6() {
        String name = "c1";
        String description = "description1";
        Card.Rarity rarity = Card.Rarity.COMMON;
        String image = "https://en.ws-tcg.com/wp/wp-content/uploads/20210519181156/DAL_W79_E003_SAMPLE.png";
        Card c1 = new Card(name, description, rarity, image);

        name = "c2";
        description = "description2";
        rarity = Card.Rarity.COMMON;
        Card c2 = new Card(name, description, rarity, image);

        name = "duplicatename";
        description = "description3";
        rarity = Card.Rarity.COMMON;
        Card c3 = new Card(name, description, rarity, image);

        name = "duplicatename";
        description = "description4";
        rarity = Card.Rarity.RARE;
        Card c4 = new Card(name, description, rarity, image);

        name = "duplicatename";
        description = "description5";
        rarity = Card.Rarity.LEGENDARY;
        Card c5 = new Card(name, description, rarity, image);

        name = "differentnamelength";
        description = "description6";
        rarity = Card.Rarity.COMMON;
        Card c6 = new Card(name, description, rarity, image);


        return Arrays.asList(c2, c1, c3, c4, c5, c6);
    }


    @Test
    public void testGetCount1() {
        List<Card> cards = getCards8();
        CardGridAdapter adapter = new CardGridAdapter(context,cards);
        assertEquals(8, adapter.getCount());
    }

    @Test
    public void testGetCount2() {
        List<Card> cards = getCards6();
        CardGridAdapter adapter = new CardGridAdapter(context,cards);
        assertEquals(6, adapter.getCount());
    }
    @Test
    public void testUpdateData() {
        List<Card> cards = getCards8();
        CardGridAdapter adapter = new CardGridAdapter(context,cards);
        adapter.updateData(getCards6());
        assertEquals(getCards6(), adapter.cards);

    }


}