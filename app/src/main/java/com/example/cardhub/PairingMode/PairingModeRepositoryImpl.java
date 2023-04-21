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
    public PairingModeRepositoryImpl(PairingModeRepositoryReceiver receiver, PairingModeData data) {
        this.data = data;
        this.receiver = receiver;
    }

    public PairingModeRepositoryImpl() {
        this.data = null;
        this.receiver = null;
    }

    @Override
    public String getUid() {
        return data.getUid();
    }

    @Override
    public void generateQR(String lobby) {
        receiver.generateQR(lobby);
    }

    @Override
    public void lobbyCreated(String lobby) {
        receiver.lobbyCreated(lobby);
    }

    @Override
    public void joinedLobby(String lobby) {
        receiver.joinedLobby(lobby);
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
