package com.example.cardhub.PairingMode;

import android.graphics.Bitmap;
import android.view.WindowManager;

import com.google.firebase.firestore.FirebaseFirestore;

public interface PairingModeRepository {
    public String getUid();

    public FirebaseFirestore getDb();

    public void generateQR(String lobby);

    void lobbyCreated();

    void joinedLobby();

    void generateLobby();

    void joinLobby(String lobby);
}
