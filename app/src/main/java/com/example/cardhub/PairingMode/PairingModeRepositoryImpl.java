package com.example.cardhub.PairingMode;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PairingModeRepositoryImpl implements PairingModeRepository{
    PairingModeData data;

    PairingModeRepositoryReceiver receiver;

    public PairingModeRepositoryImpl(PairingModeRepositoryReceiver receiver) {
        data = new PairingModeData(this, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance());
        this.receiver = receiver;
    }

    @Override
    public String getUid() {
        return data.getUid();
    }

    public FirebaseFirestore getDb() {
        return data.getDb();
    }

    @Override
    public void generateQR(String lobby) {
        receiver.generateQR(lobby);
    }

    @Override
    public void lobbyCreated() {
        receiver.lobbyCreated();
    }

    @Override
    public void joinedLobby() {
        receiver.joinedLobby();
    }

    @Override
    public void generateLobby() {
        data.generateLobby();
    }

    @Override
    public void joinLobby(String lobby) {
        data.joinLobby(lobby);
    }
}
