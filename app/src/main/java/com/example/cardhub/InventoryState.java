package com.example.cardhub;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InventoryState {
    List<Card> cards = new ArrayList<>();
    InventoryRepository repository;

    /**
     * Initialize the class with a new repository
     */
    public InventoryState() {
        this.repository = new InventoryRepositoryImpl();
    }

    /**
     * Get the cards from the repository and add to the state
     * @return cards
     */
    public void requestCards(GetCardsCallback inventoryActivityCallback) {
        repository.requestCards(new GetCardsCallback(this), inventoryActivityCallback);
    }

    /**
     * Get a specific card from the state
     * @param index index of the card
     * @return the card at {@code index}
     */
    public Card getCard(int index) {
        return cards.get(index);
    }

    /**
     * Add a card on the database and refresh local state
     * @param card the card to add
     */
    public void addCard(Card card) {
        repository.addCard(card);
        requestCards();
    }

    /**
     * Remove a given card from the database
     * @param card the card to remove
     */
    public void removeCard(Card card) {
        repository.removeCard(card);
        requestCards();
    }


    public class GetCardsCallbackImpl implements GetCardsCallback{
        InventoryState base;
        GetCardsCallbackImpl(final InventoryState base) {
            this.base = base;
        }

        @Override
        public void run(final List<Card> cards, GetCardsCallback inventoryActivityCallback) {
            base.cards = cards;
            inventoryActivityCallback.run(cards);
        }
    }
}


