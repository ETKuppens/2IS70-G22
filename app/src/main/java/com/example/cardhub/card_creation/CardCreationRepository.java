package com.example.cardhub.card_creation;

import com.example.cardhub.inventory.Card;

/**
 * Repository Class for creating cards.
 * @author Rijkman
 */
public class CardCreationRepository {
    //Database instance
    CardCreationData data;
    //Constructor
    CardCreationRepository() {
        data = new CardCreationData();
    }


    /**
     * Publishes a given card to the database pool.
     * @pre The card is valid
     * @param c The card that needs to be published
     */
    public void publishCard(Card c) {
        data.publishCard(c);
    }
}
