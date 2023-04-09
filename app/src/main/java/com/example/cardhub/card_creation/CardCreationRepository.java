package com.example.cardhub.card_creation;

import com.example.cardhub.inventory.Card;

public class CardCreationRepository {
    CardCreationData data;

    CardCreationRepository() {
        data = new CardCreationData();
    }

    public void publishCard(Card c) {
        data.publishCard(c);
    }
}
