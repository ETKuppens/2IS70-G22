package com.example.cardhub.inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryState implements InventoryRepositoryReceiver {

    List<Card> cards = new ArrayList<>();
    InventoryActivity activity;
    InventoryRepository repository;

    /**
     * Creates an Inventory state for the InventoryActivity
     *
     * @param activity the activity the state is responsible for
     */
    public InventoryState(InventoryActivity activity) {
        this.activity = activity;
        this.repository = new InventoryRepositoryImpl(this);
    }

    @Override
    public void receiveCardsResponse(List<Card> cards) {
        this.cards = cards;
        activity.updateGrid();
    }

    @Override
    public void requestUserCards() {
        repository.requestUserCards();
    }

    /**
     * Returns the list of cards.
     *
     * @return all the cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Get a specific card.
     *
     * @param i card at index to get
     * @return the specific card
     */
    public Card getCard(int i) {
        return cards.get(i);
    }
}
