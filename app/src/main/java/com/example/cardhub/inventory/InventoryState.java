package com.example.cardhub.inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryState implements InventoryRepositoryReceiver {

    List<Card> displayCards = new ArrayList<>();
    List<Card> userCards = new ArrayList<>();
    InventoryActivity activity;
    InventoryRepository repository;

    public boolean showingInventory = true;

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
        this.displayCards.clear();
        this.displayCards.addAll(cards);

        if (showingInventory) {
            this.userCards.clear();
            this.userCards.addAll(cards);
        }
        activity.updateGrid();
    }

    public void sortCards(CardSorter.SortAttribute attribute) {
        CardSorter.Sort(this.displayCards, attribute, CardSorter.SortOrder.DEFAULT);
        activity.updateGrid();
    }

    public void toggleCollection() {
        if (showingInventory) {
            repository.requestAllCards();
        } else {
            repository.requestUserCards();
        }
        showingInventory = !showingInventory;

        activity.updateCollectionButton();
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
    public List<Card> getDisplayCards() {
        return displayCards;
    }

    /**
     * Get a specific card.
     *
     * @param i card at index to get
     * @return the specific card
     */
    public Card getCard(int i) {
        return displayCards.get(i);
    }
}
