package com.example.cardhub;

import java.util.ArrayList;
import java.util.List;

public class InventoryRepositoryImpl implements InventoryRepository {
    List<Card> cards;

    /**
     * Generate local mock cards for now
     * will fetch from firebase (without constructor) later
     */
    public InventoryRepositoryImpl() {
        cards = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            addCard(new Card("Card: " + i, Card.Rarity.COMMON, R.drawable.placeholder));
        }
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public void removeCard(Card card) {
        cards.remove(card);
    }

    @Override
    public void addCard(Card card) {
        cards.add(card);
    }
}
