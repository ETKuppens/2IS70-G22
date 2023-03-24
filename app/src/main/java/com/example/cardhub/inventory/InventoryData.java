package com.example.cardhub.inventory;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cardhub.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interact with the database with information related to the database
 */
public class InventoryData {
    FirebaseFirestore db;
    InventoryRepository repository;

    /**
     * Get the database instance
     */
    public InventoryData(InventoryRepository repository) {
        db = FirebaseFirestore.getInstance();
        this.repository = repository;
    }


    /**
     * Request the cards that belong to a specific user from the database
     *
     * @return list of cards
     */
    public void requestCards() {
        final List<Map<String, Object>> cards = new ArrayList<>();
        cards.add(new HashMap<>());
        cards.get(0).put("EMPTY", null);

        db.collection("cards").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            cards.clear();
                            for (DocumentSnapshot documentSnapshots : task.getResult()) {
                                cards.add(documentSnapshots.getData());
                            }
                            repository.cardRequestCallback(cards);
                            Log.d("CARDREQUEST", "success");
                        } else {
                            Log.d("CARDREQUEST", "fail: " + task.getException());
                        }
                    }
                });
    }

    /**
     * Remove a card from the inventory of a user int the database
     *
     * @param card the card to remove
     */
    public void removeCard(Card card) {

    }

    /**
     * Add a card to the inventory of a user int the database
     *
     * @param card the card to add
     */
    public void addCard(Card card) {

    }

}