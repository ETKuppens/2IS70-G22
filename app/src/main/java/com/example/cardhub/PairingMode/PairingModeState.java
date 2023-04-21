package com.example.cardhub.PairingMode;

import android.graphics.Bitmap;
import android.view.WindowManager;

import com.google.firebase.firestore.FirebaseFirestore;

public class PairingModeState implements PairingModeRepositoryReceiver {
    PairingModeActivity activity;

    PairingModeRepository repository;

    public PairingModeState(PairingModeActivity activity) {
        this.activity = activity;
        repository = new PairingModeRepositoryImpl(this);
    }

    @Override
    public String getUid() {
        return repository.getUid();
    }

    public FirebaseFirestore getDb() {
        return repository.getDb();
    }

    public Bitmap generateBitmap(String code, WindowManager manager){
        return repository.generateBitmap(code, manager);
    }
}
