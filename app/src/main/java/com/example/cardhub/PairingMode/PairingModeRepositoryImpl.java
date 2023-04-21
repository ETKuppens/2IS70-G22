package com.example.cardhub.PairingMode;

import android.graphics.Bitmap;
import android.view.WindowManager;

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

    public Bitmap generateBitmap(String code, WindowManager manager){
        return data.generateBitmap(code, manager);
    }
}
