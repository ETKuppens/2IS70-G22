package com.example.cardhub.card_creation;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cardhub.inventory.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

/**
 * Class responsible for uploading a new card to the card pool in the database.
 * @author Tulgar
 */
public class CardCreationData {
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseStorage storage;

    CardCreationData(FirebaseFirestore db, FirebaseAuth auth, FirebaseStorage storage) {
        this.db = db;
        this.auth = auth;
        this.storage = storage;
    }

    /**
     * Publishes a given card to the database pool.
     * @param c The card that needs to be published
     */
    public void publishCard(Card c) {
        final StorageReference ref = storage.getReference("card_images/");

        StorageReference imageRef = ref.child(c.NAME + ".png");
        imageRef.putFile(Uri.parse(c.IMAGE_URL)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {

                    imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                final String link = task.getResult().toString();

                                HashMap<String, Object> cardObj = new HashMap<>();
                                cardObj.put("name", c.NAME);
                                cardObj.put("description", c.DESCRIPTION);
                                cardObj.put("rarity", c.RARITY.toString());
                                cardObj.put("imageurl", link );

                                db.collection("cards/").add(cardObj).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            db.collection("users/" + auth.getUid() + "/cards/").add(cardObj).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("CARD_CREATION", "card added to creator inventory.");
                                                    } else {
                                                        Log.d("CARD_CREATION", "failed to add card to creator inventory: " + task.getException());
                                                    }
                                                }
                                            });
                                        } else {
                                            Log.d("CARD_CREATION", "failed to add card to card pool: " + task.getException());
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}
