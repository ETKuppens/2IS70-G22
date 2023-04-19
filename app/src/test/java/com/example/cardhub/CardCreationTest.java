package com.example.cardhub;

import android.content.Context;
import android.net.Uri;

import com.example.cardhub.card_creation.CardCreationRepository;
import com.example.cardhub.card_creation.CardCreationState;
import com.example.cardhub.inventory.Card;

import org.junit.Before;
import org.junit.Test;

public class CardCreationTest {
    CardCreationState testState;
    @Before
    public void initState() {
        this.testState = new CardCreationState(new CardCreationRepository() {

            @Override
            public void publishCard(Card c) {
                return;
            }
        });
    }

    @Test
    public void testNullName() {

        testState.setRarity(Card.Rarity.COMMON);
        testState.setCurrentDescription("ABC");
        boolean passed = false;
        try {
            testState.publishCard();
        } catch (NullPointerException e) {
            passed = true;
        }
        assert passed;
    }

    @Test
    public void testNullRarity() {
        testState.setCurrentName("abc");
        testState.setCurrentDescription("ABC");
        boolean passed = false;
        try {
            testState.publishCard();
        } catch (NullPointerException e) {
            passed = true;
        }
        assert passed;
    }

    @Test
    public void testNullDescription() {
        testState.setCurrentName("abc");
        testState.setRarity(Card.Rarity.COMMON);
        boolean passed = false;
        try {
            testState.publishCard();
        } catch (NullPointerException e) {
            passed = true;
        }
        assert passed;
    }

}
