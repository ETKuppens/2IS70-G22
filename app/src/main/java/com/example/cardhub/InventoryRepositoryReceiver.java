package com.example.cardhub;

import java.util.List;

/**
 * Binds a state to an InventoryRepository
 */
public interface InventoryRepositoryReceiver {
    /**
     * The function that will be triggered after
     * {@code this.requestCards()} returns with a response
     * @param cards the cards in the response
     */
    public void receiveCardsResponse(List<Card> cards);

    /**
     * Make a request for the cards
     */
    public void requestCards();

}
