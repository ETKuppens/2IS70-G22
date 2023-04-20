package com.example.cardhub.map;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cardhub.inventory.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Interact with Firebase to facilitate the map view.
 */
public class MapData {
    MapRepository repo;
    FirebaseFirestore db;
    FirebaseAuth auth;

    public MapData(MapRepository repo, FirebaseFirestore db, FirebaseAuth auth) {
        this.repo = repo;
        this.db = db;
        this.auth = auth;
    }

    /**
     * Make a request to get all available card packs from firebase.
     */
    public void requestPacks() {
        db.collection("cardpacks/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Map<String, Object>> packs = task.getResult().getDocuments().stream()
                            .map((DocumentSnapshot snapshot) -> snapshot.getData())
                            .collect(Collectors.toList());


                    repo.receivePacks(packs);


                } else {
                    Log.d("CARDPACKS", "failed to get cardpacks: " + task.getException());
                }
            }
        });
    }

    /**
     * Get a random card from all available cards that are the specified rarity.
     * @param rarity the rarity of the random card that is requested
     */
    public void acquireRandomCard(Card.Rarity rarity) {
        db.collection("cards/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Map<String, Object>> cards = task.getResult().getDocuments().stream()
                            .map((DocumentSnapshot snapshot) -> snapshot.getData())
                            .collect(Collectors.toList());
                    List<Map<String, Object>> cardsWithMatchingRarity = cards.stream().filter((card) ->
                            (Card.Rarity.valueOf((String) card.get("rarity")) == rarity))
                            .collect(Collectors.toList());
                    Map<String, Object> acquiredCard = cardsWithMatchingRarity.get(ThreadLocalRandom.current().nextInt(cardsWithMatchingRarity.size()));
                    db.collection("users/" + auth.getUid() + "/cards").add(acquiredCard).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {

                                repo.acquireRandomCardCallback(acquiredCard);
                                Log.d("ACQUISITION", "added card successfully");
                            } else {
                                Log.d("ACQUISITION", "failed to add card: " + task.getException());
                            }
                        }
                    });
                } else {
                    Log.d("ACQUISITION", "failed to get cards");
                }
            }
        });
    }
}
