package com.example.cardhub.TradingMode;

import static android.content.ContentValues.TAG;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.Batch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.internal.ObjectConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TradingSessionData {
    TradingSessionRepository repository;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;
    private String lid;
    private String clientid;
    private String currentPlayer;
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
        docRef.collection("cardDiffs_" + otherPlayer).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                Log.d("ANYTHING", String.valueOf(value.size()));

                if (value != null && value.size() > 0)  {
                    Log.d(TAG, "Current data: " + value.getDocuments());

                    List<Map<String, Object>> diffList = new ArrayList<>();

                    for (DocumentSnapshot diff: value.getDocuments()) {
                        diffList.add(diff.getData());
                        Log.d("DOCUMENTSNAPSHOT", "onEvent: " + diff.getData());
                    }

                    Log.d("CARD", String.valueOf(diffList.get(0)));

                    if (diffList != null && diffList.size() > 0) {
                        repository.receiveUpdate(diffList);
                    }
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
                            currentPlayer = "playerA";
                            otherPlayer = "playerB";
                        } else if (playerBName.equals(clientid)) {
                            currentPlayer = "playerB";
                            otherPlayer = "playerA";
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
        data.put("acceptance_" + currentPlayer, true);

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
        data.put("acceptance_" + currentPlayer, false);

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
        List<Map<String, Object>> data = diffs.stream().map(diff  -> diff.serialize()).collect(Collectors.toList());

//        Log.d(TAG, "DocumentSnapshot written with ID: " + db.collection("lobbies").document(lid).document.getId());
        WriteBatch batch = db.batch();

        for (Map<String, Object> diff : data) {
            DocumentReference id = docRef.collection("cardDiffs_" + currentPlayer).document();
            batch.set(id, diff);
        }

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("BATCH", "success");
                    repository.changeProposedCardsConfirm(clientID);
                } else {
                    Log.d("BATCH", "fail");
                }
            }
        });

//        docRef.collection("cardDiffs_" + currentPlayer)
//                .add(data)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });

//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//
//                        HashMap<>
//                        document.get("cardDiffs_"  + currentPlayer);
//
//                        List<CardDiff> diffs2 = new ArrayList<>();
//                        diffs2.addAll(diffs);
//                        Map<String, Object> data = new HashMap<>();
//                        data.put("cardDiffs_" + currentPlayer, diffs2);
//
//                        docRef.set(data, SetOptions.merge())
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Log.d("cardDiffs_" + currentPlayer, "Updated succesfully!");
//                                        repository.changeProposedCardsConfirm(clientID);
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.w("cardDiffs_" + currentPlayer, "An error was encountered while updating!", e);
//                                    }
//                                });
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
    }
}
