package com.example.cardhub;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
The repository that stores cards for the inventory
 */
public interface InventoryRepository {
    /**
     * Get all cards that belong to the user
     */
    public void requestCards(Consumer<List<Card>> callback, Consumer<List<Card>> updateGridCallback);

    void getCards(List<Map<String, Object>> cardsRaw, Consumer<List<Card>> callback);

    /**
     * Remove a card from the users inventory on the database
     * @param card the card to remove
     */
    public void removeCard(Card card);

    /**
     * Add a card to the users inventory on the database
     * @param card the card to add
     */
    public void addCard(Card card);
}
