package com.example.cardhub;

import java.util.List;

/**
The repository that stores cards for the inventory
 */
public interface InventoryRepository {
    /**
     * Get all cards that belong to the user
     */
    public List<Card> getCards();

    /**
     * Remove a card from the users inventory
     */
    public void removeCard(Card card);

    /**
     * Add a card to the users inventory
     */
    public void addCard(Card card);
}
