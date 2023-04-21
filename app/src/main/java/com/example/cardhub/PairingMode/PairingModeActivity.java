package com.example.cardhub.PairingMode;

import static android.content.ContentValues.TAG;

import static androidx.core.content.ContextCompat.getSystemService;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cardhub.R;
import com.example.cardhub.TradingMode.TradeModeActivity;
import com.example.cardhub.authentification.LoginActivity;
import com.example.cardhub.collector_navigation.CollectorBaseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class PairingModeActivity extends CollectorBaseActivity {
    private FirebaseAuth mAuth;

    PairingModeState state;
    String lobby;
    Map<String, Object> lobbyMap;
    public ImageView qrCodeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairingmode);
        setupNav();
        // initializing all variables.

        Button generateQrBtn = findViewById(R.id.idBtnGenerateQR);
        Button scanQrBtn = findViewById(R.id.idScanQrCode);

        state= new PairingModeState(this);
        qrCodeIV = findViewById(R.id.idIVQrcode);
        mAuth = FirebaseAuth.getInstance();
        //db = FirebaseFirestore.getInstance();
        //FirebaseUser user = mAuth.getCurrentUser();
        //uid = user.getUid();

        // initializing onclick listener for button.
        generateQrBtn.setOnClickListener(v -> {
            state.generateLobby();
        });

        // Scan for a QR code
        scanQrBtn.setOnClickListener(v ->{
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setPrompt("Scan a barcode or QR Code");
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.initiateScan();
        });
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
                state.joinLobby(intentResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_pairingmode;
    }

    @Override
    public int getBottomNavigationMenuItemId() {
        return R.id.action_trading;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            this.startActivity(intent);
            startActivity(intent);
        }
    }

    /**
     * Generate a QR code describing {@code code}.
     */
    public void generateQR(String code) {
        // below line is for getting
        // the window-manager service.
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        final float[] NEGATIVE = {
                -1.0f,     0,     0,    0, 255, // red
                0, -1.0f,     0,    0, 255, // green
                0,     0, -1.0f,    0, 255, // blue
                0,     0,     0, 1.0f,   0  // alpha
        };
        qrCodeIV.setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
        // the bitmap is set inside our image
        // view using .setimagebitmap method.
        qrCodeIV.setImageBitmap(state.generateBitmap(code,manager));
    }

    public void lobbyCreated(String lobby) {
        Intent intent = new Intent(getApplicationContext(), TradeModeActivity.class);
        intent.putExtra("lobbyid", lobby);
        intent.putExtra("clientid", state.getUid());

        // Stop showing the QR code
        qrCodeIV.clearColorFilter();
        qrCodeIV.setImageDrawable(null);
        qrCodeIV.invalidate();

        startActivity(intent);
    }

    public void joinedLobby(String lobby) {
        Toast.makeText(PairingModeActivity.this, "Logged in on " + lobby, Toast.LENGTH_SHORT).show();
        Log.d("WORRY", "DocumentSnapshot successfully written!");
        Intent intent = new Intent(getApplicationContext(), TradeModeActivity.class);
        intent.putExtra("lobbyid", lobby);
        intent.putExtra("clientid", state.getUid());
        startActivity(intent);
    }
}