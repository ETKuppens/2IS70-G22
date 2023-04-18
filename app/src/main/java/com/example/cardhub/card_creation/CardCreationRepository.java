package com.example.cardhub.card_creation;

import com.example.cardhub.inventory.Card;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class CardCreationRepository {
    CardCreationData data;

    CardCreationRepository() {
        data = new CardCreationData(FirebaseFirestore.getInstance(),
                FirebaseAuth.getInstance(),
                FirebaseStorage.getInstance());
    }

    public void publishCard(Card c) {
        data.publishCard(c);
    }
}
