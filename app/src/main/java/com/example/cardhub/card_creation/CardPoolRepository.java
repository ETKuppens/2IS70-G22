package com.example.cardhub.card_creation;

import com.example.cardhub.inventory.Card;

public class CardPoolRepository {
    CardPoolData data;

    CardPoolRepository() {

    }

    public void publishCard(Card c) {
        data.publishCard(c);
    }
}