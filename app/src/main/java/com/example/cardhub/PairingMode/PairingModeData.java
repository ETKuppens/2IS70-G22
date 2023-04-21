package com.example.cardhub.PairingMode;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PairingModeData {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    PairingModeRepository repository;

    Bitmap bitmap;

    QRGEncoder qrgEncoder;

    public PairingModeData(PairingModeRepository repository, FirebaseAuth mAuth, FirebaseFirestore db) {
        this.repository = repository;
        this.mAuth = mAuth;
        this.db = db;
    }


    public String getUid() {
        FirebaseUser user = mAuth.getCurrentUser();
        return user.getUid();
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public Bitmap generateBitmap(String code, WindowManager manager){
        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = Math.min(width, height);
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(code, QRGContents.Type.TEXT, dimen);
        // getting our qrcode in the form of bitmap.
        bitmap = qrgEncoder.getBitmap();

        return bitmap;
    }
}
