package com.example.cardhub.PairingMode;

import static android.content.Context.WINDOW_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class PairingModeState implements PairingModeRepositoryReceiver {
    PairingModeActivity activity;

    PairingModeRepository repository;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;

    public PairingModeState(PairingModeActivity activity) {
        this.activity = activity;
        repository = new PairingModeRepositoryImpl(this);
    }

    @Override
    public String getUid() {
        return repository.getUid();
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

    @Override
    public void generateQR(String lobby) {
        activity.generateQR(lobby);
    }

    @Override
    public void lobbyCreated(String lobby) {
        activity.lobbyCreated(lobby);
    }

    @Override
    public void joinedLobby(String lobby) {
        activity.joinedLobby(lobby);
    }

    public void generateLobby() {
        repository.generateLobby();
    }

    public void joinLobby(String lobby) {
        repository.joinLobby(lobby);
    }
}

