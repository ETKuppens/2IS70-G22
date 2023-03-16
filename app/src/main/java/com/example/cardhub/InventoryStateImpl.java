package com.example.cardhub;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class InventoryStateImpl implements InventoryState {

    List<Card> cards = new ArrayList<>();
    InventoryActivity activity;
    InventoryRepository repository;

    public InventoryStateImpl(InventoryActivity activity) {
        this.activity = activity;
        this.repository = new InventoryRepositoryImpl(this);
    }

    @Override
    public void setCards(List<Card> cards) {
        this.cards = cards;
        activity.updateGrid();
    }

    @Override
    public void requestCards() {
        repository.requestCards();
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public Card getCard(int i) {
        return cards.get(i);
    }
}
