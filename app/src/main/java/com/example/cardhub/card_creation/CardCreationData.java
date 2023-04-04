package com.example.cardhub.card_creation;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cardhub.inventory.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CardCreationData {
    FirebaseFirestore db;
    FirebaseAuth

    CardCreationData() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void publishCard(Card c) {
        HashMap<String, Object> cardObj = new HashMap<>();
        cardObj.put("name", c.NAME);
        cardObj.put("description", c.DESCRIPTION);
        cardObj.put("rarity", c.RARITY);
        cardObj.put("imageurl", c.IMAGE_URL);

        db.collection("cards/").add(cardObj).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    db.collection("")
                } else {
                    Log.d("CARD_CREATION", "failed to add card to card pool");
                }
            }
        });
    }
}
