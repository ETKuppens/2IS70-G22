package com.example.cardhub.inventory;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

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
    FirebaseAuth auth;

    /**
     * Get the database instance
     */
    public InventoryData(InventoryRepository repository, FirebaseAuth auth, FirebaseFirestore db) {
        this.db = db;
        this.auth = auth;
        this.repository = repository;
    }


    /**
     * Request the cards that belong to a specific user from the database
     *
     * @return list of cards
     */
    public void requestAllCards() {
        final List<Map<String, Object>> cards = new ArrayList<>();
        cards.add(new HashMap<>());

        db.collection("cards/").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        cards.clear();
                        for (DocumentSnapshot documentSnapshots : task.getResult()) {
                            cards.add(documentSnapshots.getData());
                        }
                        repository.cardRequestCallback(cards);
                        Log.d("CARDREQUESTALL", "success");
                    } else {
                        Log.d("CARDREQUESTALL", "fail: " + task.getException());
                    }
                }
            });
    }

    public void requestUserCards() {
        final List<Map<String, Object>> cards = new ArrayList<>();
        cards.add(new HashMap<>());

        db.collection("users/" + auth.getUid() + "/cards").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            cards.clear();
                            for (DocumentSnapshot documentSnapshots : task.getResult()) {
                                cards.add(documentSnapshots.getData());
                            }
                            Log.d("CARDREQUEST", "card amount: " + cards.size());
                            repository.cardRequestCallback(cards);
                            Log.d("CARDREQUEST", "success");
                        } else {
                            Log.d("CARDREQUEST", "fail: " + task.getException());
                        }
                    }
                });
    }
}
