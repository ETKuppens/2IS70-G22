package com.example.cardhub.inventory;

import java.util.List;
import java.util.Map;

/**
 * The repository that stores cards for the inventory
 */
public interface InventoryRepository {
    /**
     * Get all cards that belong to the user
     */
    public void requestAllCards();
    public void requestUserCards();

    public void cardRequestCallback(List<Map<String, Object>> cardsRaw);

    public List<Card> processCards(List<Map<String, Object>> cardsRaw);

    /**
     * Remove a card from the users inventory on the database
     *
     * @param card the card to remove
     */
    public void removeCard(Card card);

    /**
     * Add a card to the users inventory on the database
     *
     * @param card the card to add
     */
    public void addCard(Card card);
}
