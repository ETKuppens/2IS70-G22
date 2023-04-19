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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.firestore.core.View;
import com.google.gson.internal.ObjectConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Communicate with firebase to facilitate card trading.
 */
public class TradingSessionData {
    TradingSessionRepository repository;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private String clientid;
    private String currentPlayer;
    private String otherPlayer;
    private ListenerRegistration listenerRegistration;

    public TradingSessionData(TradingSessionRepository repository,
                              FirebaseFirestore db,
                              String lobbyId,
                              String clientId) {
        this.repository = repository;
        this.clientid = clientId;
        this.docRef = db.collection("lobbies").document(lobbyId);
        this.db = db;

        getInfo();
    }

    private void startCardDiffListener() {
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

                    for (DocumentChange dc: value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            diffList.add(dc.getDocument().getData());
                        }
                    }

                    Log.d("CARD", String.valueOf(diffList.get(0)));

                    if (diffList != null && diffList.size() > 0) {
                        repository.receiveUpdate(diffList);
                    }
                }
            }
        });
    }

    private void getInfo() {
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
                        startReadyListener();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void startReadyListener() {
        listenerRegistration = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Map<String, Object> result = snapshot.getData();
                    if (result == null) return;
                    if (currentPlayer.equals("playerA")) {
                        if (
                                result.get("acceptance_playerA") == null ||
                                result.get("acceptance_playerB") == null) return;
                        boolean playerA = (boolean)result.get("acceptance_playerA");
                        boolean playerB = (boolean)result.get("acceptance_playerB");
                        if (playerA && playerB) {
                            Log.d("TRADING_SESSION", "both players ready");
                            repository.startTradeTimer();
                        } else {
                        }
                    } else if(result.get("finished") != null && (boolean)result.get("finished"))  {
                        docRef.delete();
                        repository.finishTrade();
                    }
                } else {

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
    public void cancelTradingSession(String clientID) {
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
     */
    public void acceptProposedTrade() {
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
     */
    void cancelAcceptTrade() {
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

    }

    /**
     * Remove the proposed cards from the inventories of the players that proposed them.
     * Add the cards the to the other players inventory.
     * Increment the amount of trades performed by players.
     */
    public void doTrade() {
        Log.d("TRADING", "doTrade: Starting trade");
        docRef.collection("cardDiffs_" + currentPlayer).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Map<String,Object>> playerAOfferedCards = new ArrayList<>();
                    for (DocumentSnapshot cd : task.getResult()) {
                        playerAOfferedCards.add((Map<String,Object>)cd.getData().get("card"));
                    }

                    docRef.collection("cardDiffs_" + otherPlayer).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Map<String,Object>> playerBOfferedCards = new ArrayList<>();
                                for (DocumentSnapshot cd : task.getResult()) {
                                    playerBOfferedCards.add((Map<String,Object>)cd.getData().get("card"));
                                }

                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            String playerAName = (String)task.getResult().get("playerAName");
                                            String playerBName = (String)task.getResult().get("playerBName");

                                            WriteBatch batch = db.batch();
                                            if (playerAOfferedCards.size() > 0) {
                                                List<String> offeredNamesA = playerAOfferedCards.stream()
                                                        .map(card -> (String) card.get("name")).collect(Collectors.toList());
                                                offeredNamesA.add("DO_NOT_USE_THIS_NAME");
                                                db.collection("users/" + playerAName + "/cards")
                                                        .whereIn("name", offeredNamesA)
                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (DocumentSnapshot s : task.getResult()) {
                                                                        batch.delete(s.getReference());
                                                                    }

                                                                    deletePlayerBOffers(playerAOfferedCards, playerBOfferedCards, playerBName, playerAName, batch);
                                                                }
                                                            }
                                                        });
                                            } else {
                                                deletePlayerBOffers(playerAOfferedCards, playerBOfferedCards, playerBName, playerAName, batch);
                                            }

                                            } else {
                                                Log.e("TRADING_ERROR", "failed: " + task.getException());
                                        }
                                    }
                                });
                            } else {
                                Log.e("TRADING_ERROR", "failed: " + task.getException());
                            }
                        }
                    });
                } else {
                    Log.e("TRADING_ERROR", "failed: " + task.getException());
                }
            }
        });
    }


    private void deletePlayerBOffers(List<Map<String,Object>> playerAOfferedCards, List<Map<String,Object>> playerBOfferedCards, String playerBName, String playerAName, WriteBatch batch) {
        if (playerBOfferedCards.size() > 0) {
            List<String> offeredNamesB = playerBOfferedCards.stream()
                    .map(card -> (String) card.get("name")).collect(Collectors.toList());
            offeredNamesB.add("DO_NOT_USE_THIS_NAME");
            db.collection("users/" + playerBName + "/cards")
                    .whereIn("name", offeredNamesB)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot s : task.getResult()) {
                                    batch.delete(s.getReference());
                                }

                                exchangeCards(playerAOfferedCards, playerBOfferedCards, playerBName, playerAName, batch);
                            }
                        }
                    });
        } else {
            exchangeCards(playerAOfferedCards, playerBOfferedCards, playerBName, playerAName, batch);
        }
    }

    private void exchangeCards (List<Map<String,Object>> playerAOfferedCards, List<Map<String,Object>> playerBOfferedCards, String playerBName, String playerAName, WriteBatch batch) {

        DocumentReference docRefInvA = db.collection("users/" + playerAName + "/cards").document();
        for (Map<String, Object> card : playerBOfferedCards) {
            batch.set(docRefInvA, card);
        }

        DocumentReference docRefInvB = db.collection("users/" + playerBName + "/cards").document();
        for (Map<String, Object> card : playerAOfferedCards) {
            batch.set(docRefInvB, card);
        }

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    db.collection("users").document(playerAName).update("tradesmade", FieldValue.increment(1));
                    db.collection("users").document(playerBName).update("tradesmade", FieldValue.increment(1));
                    docRef.update("finished", true);
                    listenerRegistration.remove();
                    Log.d("TRADING", "trade complete");
                    repository.finishTrade();
                } else {
                    Log.d("TRADING", "trade failed" + task.getException());
                }
            }
        });
    }
}
