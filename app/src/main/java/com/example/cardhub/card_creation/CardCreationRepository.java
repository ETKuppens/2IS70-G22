package com.example.cardhub.card_creation;

import com.example.cardhub.inventory.Card;

/**
 * Interface between Data and State class for the CardCreation
 * @author Rijkman
 */
public interface CardCreationRepository {
    /**
     * Publishes card c to the Card Pool
     * @param c the card to publish
     */
    public abstract void publishCard(Card c);
}
