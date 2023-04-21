package com.example.cardhub.PairingMode;

import android.graphics.Bitmap;
import android.view.WindowManager;

import com.google.firebase.firestore.FirebaseFirestore;

public interface PairingModeRepositoryReceiver {
    public String getUid();

    public FirebaseFirestore getDb();

    public Bitmap generateBitmap(String code, WindowManager manager);
}
