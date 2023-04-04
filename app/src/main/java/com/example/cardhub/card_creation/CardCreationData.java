package com.example.cardhub.card_creation;

import com.example.cardhub.inventory.Card;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CardCreationData {
    FirebaseFirestore db;

    CardCreationData() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void publishCard(Card c) {
        HashMap<String, Object> cardObj = new HashMap<>();
        cardObj.put("name", c.NAME);
        cardObj.put("description", c.DESCRIPTION);
        cardObj.put("rarity", c.RARITY);
        cardObj.put("imageurl", c.IMAGE_URL);

        db.collection("cards/").add(cardObj);
    }
}
