package com.example.cardhub.PairingMode;

import static android.content.ContentValues.TAG;


import android.graphics.Bitmap;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.HashMap;
import java.util.Map;


public class PairingModeData {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    PairingModeRepository repository;



    String lobby;

    Map<String, Object> lobbyMap;

    public PairingModeData(PairingModeRepository repository, FirebaseAuth mAuth, FirebaseFirestore db) {
        this.repository = repository;
        this.mAuth = mAuth;
        this.db = db;
    }

    /**
     * Get the current user's uid.
     *
     * @return The current user's uid.
     */
    public String getUid() {
        FirebaseUser user = mAuth.getCurrentUser();
        return user.getUid();
    }

    /**
     * Generate a HashMap for a new lobby and adds the given uid to the playerAName field.
     *
     * @param uid The uid of the player that creates the lobby.
     * @return The HashMap containing the lobby data.
     */
    private HashMap generateLobbyMap(String uid) {
        HashMap lobbyMap = new HashMap();

        // Add lobby data
        lobbyMap.put("playerAName", uid);
        lobbyMap.put("playerBName", "");

        return lobbyMap;
    }

    /**
     * Generate a new lobby and add it to the database.
     */
    public void generateLobby() {
        lobbyMap = generateLobbyMap(mAuth.getUid());

        // Add lobby
        db.collection("lobbies").add(lobbyMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    boolean active = true;
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        lobby = documentReference.getId();
                        Log.d(TAG, "DocumentSnapshot written with ID: " + lobby);
                        // Generate QR code from given string code
                        repository.generateQR(lobby);
                        final DocumentReference docRef = db.collection("lobbies").document(lobby);
                        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w(TAG, "Listen failed.", e);
                                    return;
                                }

                                if (snapshot != null && snapshot.exists() && !snapshot.getData().get("playerBName").equals("")&&active) {
                                    repository.lobbyCreated(lobby);
                                    active = false;
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    /**
     * Join a lobby. Puts the other player's UID in the lobby.
     *
     * @param lobby String of the lobby to join.
     */
    public void joinLobby(String lobby) {
        // if the intentResult is not null we'll set
        // the content and format of scan message
        DocumentReference docRef = db.collection("lobbies").document(lobby);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Read data
                    lobbyMap = document.getData();

                    // Modify data
                    lobbyMap.put("playerBName", mAuth.getUid());

                    // Write data
                    db.collection("lobbies").document(lobby)
                            .set(lobbyMap)
                            .addOnSuccessListener(aVoid -> {

                                repository.joinedLobby(lobby);
                            })
                            .addOnFailureListener(e -> Log.w("WoRRY", "Error writing document", e));
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

}
