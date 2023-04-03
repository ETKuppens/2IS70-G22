package com.example.cardhub.TradingMode;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cardhub.inventory.InventoryRepositoryReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Set;

/**
 * Class implementing the TradingSessionRepository interface
 */
public class TradingSessionRepositoryImpl implements TradingSessionRepository {
    InventoryRepositoryReceiver receiver;

    // Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String lid;

    public TradingSessionRepositoryImpl(String lid) {
        this.lid = lid;
    }

    @Override
    public void cancelTradingSession(int clientID){
        db = FirebaseFirestore.getInstance();
        // Problem: How do we get the lobby id? Can we pass this instead since we can receive the uid from the FirebaseAuth instance.
    }

    @Override
    public void cancelTradingSessionConfirm(int clientID) {

    }

    @Override
    public void acceptProposedTrade(int clientID) {

    }

    @Override
    public void cancelAcceptTrade(int clientID) {

    }

    @Override
    public void changeProposedCards(int clientID, Set<CardDiff> diffs) {

    }

    @Override
    public void changeProposedCardsConfirm(int clientID) {

    }
}
