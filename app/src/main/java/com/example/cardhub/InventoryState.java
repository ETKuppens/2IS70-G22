package com.example.cardhub;

import java.util.ArrayList;
import java.util.List;

public class InventoryState {
    List<Card> cards = new ArrayList<>();
    InventoryRepository repository;

    public InventoryState() {
        repository = new InventoryRepositoryImpl();
    }

    public List<Card> getCards() {
        cards = repository.getCards();
        return cards;
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    public void addCard(Card card) {
        repository.addCard(card);
        getCards();
    }

    public void removeCard(Card card) {
        repository.removeCard(card);
        getCards();
    }
}
