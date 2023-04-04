package com.example.cardhub.TradingMode;

import static android.content.ContentValues.TAG;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TradingSessionData {
    TradingSessionRepository repository;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;
    private String lid;
    private String clientid;
    private String otherPlayer;

    public TradingSessionData(TradingSessionRepository repository, String lid, String clientid) {
        this.repository = repository;
        this.lid = lid;
        this.clientid = clientid;
        this.docRef = db.collection("lobbies").document(lid);

        getInfo();
    }

    public void startCardDiffListener() {
        // Run card diff listener
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                // Server changes only so hasPendingWrites should be false
                if (snapshot != null && snapshot.exists() && !snapshot.getMetadata().hasPendingWrites()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());

                    List<Map<String, Object>> otherCardDiffs = (List<Map<String, Object>>) snapshot.get("cardDiffs_" + otherPlayer);

                    // Call card change function

                    repository.receiveUpdate(otherCardDiffs);
                }
            }
        });
    }

    public void getInfo() {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        String playerAName = document.getString("playerAName");
                        String playerBName = document.getString("playerBName");

                        // Find other player clientid
                        if (playerAName.equals(clientid)) {
                            otherPlayer = playerBName;
                        } else {
                            otherPlayer = playerAName;
                        }

                        startCardDiffListener();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * Request the server to cancel the trading session.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     *                 identify which side of the trading session has requested the trading session to be cancelled.
     */
    void cancelTradingSession(String clientID) {
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        repository.cancelTradingSessionConfirm(clientID);

                        Log.d("lobby", "Successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("lobby", "Error deleting lobby", e);
                    }
                });
    }

    /**
     * Request the server to accept the currently proposed trade.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     *                 identify which side of the trading session has requested to accept the proposed trade.
     */
    void acceptProposedTrade(String clientID) {
        Map<String, Object> data = new HashMap<>();
        data.put("acceptance" + "_" + clientID, true);

        docRef.set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("acceptance", "Updated succesfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("acceptance", "An error was encountered while updating!", e);
                    }
                });
    }

    /**
     * Send a confirmation message to the server that the current trade acceptance has been
     * canceled correctly.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     *                 identify which side of the trading session has requested to cancel the trade accept request.
     */
    void cancelAcceptTrade(String clientID) {
        Map<String, Object> data = new HashMap<>();
        data.put("acceptance" + "_" + clientID, false);

        docRef.set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("acceptance", "Updated succesfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("acceptance", "An error was encountered while updating!", e);
                    }
                });
    }

    /**
     * Request the server to update the other client with a difference in proposed cards.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     *                 identify which side of the trading session has requested to update their proposed cards.
     * @param diffs    a set of CardDiffs that should be applied to the other clients' instance of
     *                 TradingSession.
     */
    void changeProposedCards(String clientID, Set<CardDiff> diffs) {
        List<CardDiff> diffs2 = new ArrayList<>();
        diffs2.addAll(diffs);
        Map<String, Object> data = new HashMap<>();
        data.put("cardDiffs_" + clientID, diffs2);

        docRef.set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("cardDiffs_" + clientID, "Updated succesfully!");
                        repository.changeProposedCardsConfirm(clientID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("cardDiffs_" + clientID, "An error was encountered while updating!", e);
                    }
                });
    }
}
