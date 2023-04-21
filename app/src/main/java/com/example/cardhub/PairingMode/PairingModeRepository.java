package com.example.cardhub.PairingMode;

import android.graphics.Bitmap;
import android.view.WindowManager;

import com.google.firebase.firestore.FirebaseFirestore;

public interface PairingModeRepository {
    public String getUid();

    public void generateQR(String lobby);

    void lobbyCreated(String lobby);

    void joinedLobby(String lobby);

    void generateLobby();

    void joinLobby(String lobby);
}
