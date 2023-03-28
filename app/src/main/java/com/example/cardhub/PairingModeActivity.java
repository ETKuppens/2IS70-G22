package com.example.cardhub;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PairingModeActivity extends AppCompatActivity {
    private ImageView qrCodeIV;
    private FirebaseFirestore db;
    private TextView tv_lobby;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    String uid;
    String lobby;
    Map<String, Object> lobbyMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairingmode);

        // initializing all variables.
        qrCodeIV = findViewById(R.id.idIVQrcode);
        Button generateQrBtn = findViewById(R.id.idBtnGenerateQR);
        Button scanQrBtn = findViewById(R.id.idScanQrCode);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        tv_lobby = findViewById(R.id.textView_lobby);
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // initializing onclick listener for button.
        generateQrBtn.setOnClickListener(v -> {
            // Initialize variables
            uid = user.getUid();
            lobbyMap = generateLobbyMap(uid);

            // Add lobby
            db.collection("lobbies")
                    .add(lobbyMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            lobby = documentReference.getId();
                            Log.d(TAG, "DocumentSnapshot written with ID: " + lobby);
                            // Generate QR code from given string code
                            generateQRCode(lobby);
                            tv_lobby.setText(lobby);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        });

        // Scan for a QR code
        scanQrBtn.setOnClickListener(v ->{
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setPrompt("Scan a barcode or QR Code");
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.initiateScan();
        });
    }

    /**
     * Generate a QR code describing {@code code}.
     */
    private void generateQRCode(String code) {
        // below line is for getting
        // the window-manager service.
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

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
        //changing the colors of the QR code to make it easier to detect
        final float[] NEGATIVE = {
                -1.0f,     0,     0,    0, 255, // red
                0, -1.0f,     0,    0, 255, // green
                0,     0, -1.0f,    0, 255, // blue
                0,     0,     0, 1.0f,   0  // alpha
        };
        qrCodeIV.setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
        // the bitmap is set inside our image
        // view using .setimagebitmap method.
        qrCodeIV.setImageBitmap(bitmap);
    }

    /**
     * Generate a HashMap for a new lobby.
     */
    private HashMap generateLobbyMap(String uid) {
        HashMap lobbyMap = new HashMap();

        // Add lobby data
        lobbyMap.put("playerAName", uid);
        lobbyMap.put("playerBName", "");
        lobbyMap.put("playerACards", "...");
        lobbyMap.put("playerBCards", "...");
        lobbyMap.put("playerAConfirmation", false);
        lobbyMap.put("playerBConfirmation", false);

        return lobbyMap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                lobby = intentResult.getContents();
                // TODO: Read data
                // TODO: Modify data
                // TODO: Write data
                tv_lobby.setText(lobby);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}