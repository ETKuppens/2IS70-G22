package com.example.cardhub.TradingMode;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cardhub.TradeModeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Set;

public class TradingSessionData {
    TradingSessionRepository repository;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;
    private String lid;

    public TradingSessionData(TradingSessionRepository repository, String lid) {
        this.repository = repository;
        this.lid = lid;
        this.docRef = db.collection("lobbies").document(lid);
    }

    /**
     * Request the server to cancel the trading session.
     *
     * @param clientID the ID of the application instance that will be used by the server to
     *                 identify which side of the trading session has requested the trading session to be cancelled.
     */
    void cancelTradingSession(int clientID) {
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
    void acceptProposedTrade(int clientID) {
        docRef.update("acceptance" + "_" + clientID, true)
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
    void cancelAcceptTrade(int clientID) {
        docRef.update("acceptance" + "_" + clientID, false)
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
    void changeProposedCards(int clientID, Set<CardDiff> diffs) {
        docRef.update("cardDiffs_" + clientID, diffs)
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
