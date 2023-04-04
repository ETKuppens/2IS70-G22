package com.example.cardhub.TradingMode;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cardhub.inventory.InventoryRepositoryReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Set;

/**
 * Class implementing the TradingSessionRepository interface
 */
public class TradingSessionRepositoryImpl implements TradingSessionRepository {
    TradingSessionData data;
    TradingSessionRepositoryReceiver receiver;

    public TradingSessionRepositoryImpl (TradingSessionRepositoryReceiver receiver, String lid) {
        data = new TradingSessionData(this, lid);
        this.receiver = receiver;
    }

    @Override
    public void cancelTradingSession(int clientID){
        data.cancelTradingSession(clientID);
    }

    @Override
    public void cancelTradingSessionConfirm(int clientID) {

    }

    @Override
    public void acceptProposedTrade(int clientID) {
        data.acceptProposedTrade(clientID);
    }

    @Override
    public void cancelAcceptTrade(int clientID) {
        data.cancelAcceptTrade(clientID);
    }

    @Override
    public void changeProposedCards(int clientID, Set<CardDiff> diffs) {
        data.changeProposedCards(clientID, diffs);
    }

    @Override
    public void changeProposedCardsConfirm(int clientID) {

    }
}
