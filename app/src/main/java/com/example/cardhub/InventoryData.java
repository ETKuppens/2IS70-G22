package com.example.cardhub;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interact with the database with information related to the database
 */
public class InventoryData {
    FirebaseFirestore db;

    /**
     * Get the database instance
     */
    public InventoryData() {
        db = FirebaseFirestore.getInstance();
    }


    /**
     * Request the cards that belong to a specific user from the database
     * @return list of cards
     */
    public void requestCards(Consume<List<Map<String,Object>>,Consumer<List<Card>>> callback,
                             Consumer<List<Card>> callback2,
                             Consumer<List<Card>> updateGridCallback
                             ) {
        db.collection("users/IwbZhyP69IZFqJdJ9Lcj/cards").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<Map<String,Object>> cards = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshots : task.getResult()) {
                            cards.add(documentSnapshots.getData());
                        }
                        callback.accept(cards, callback2, updateGridCallback);
                        Log.d("CARDREQUEST", "success");
                    } else {
                        Log.d("CARDREQUEST", "fail: " + task.getException());
                    }
                }
        });
    }

    /**
     * Remove a card from the inventory of a user int the database
     * @param card the card to remove
     */
    public void removeCard(Card card) {

    }

    /**
     * Add a card to the inventory of a user int the database
     * @param card the card to add
     */
    public void addCard(Card card) {

    }

}
