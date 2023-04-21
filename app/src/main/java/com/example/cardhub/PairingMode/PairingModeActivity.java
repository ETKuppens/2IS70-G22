package com.example.cardhub.PairingMode;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
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


public class PairingModeActivity extends CollectorBaseActivity {
    private ImageView qrCodeIV;
    //private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    PairingModeState state;
    //Bitmap bitmap;
    //QRGEncoder qrgEncoder;
    //String uid;
    String lobby;
    Map<String, Object> lobbyMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairingmode);
        setupNav();
        // initializing all variables.

        qrCodeIV = findViewById(R.id.idIVQrcode);
        Button generateQrBtn = findViewById(R.id.idBtnGenerateQR);
        Button scanQrBtn = findViewById(R.id.idScanQrCode);

        state= new PairingModeState(this);
        mAuth = FirebaseAuth.getInstance();
        //db = FirebaseFirestore.getInstance();
        //FirebaseUser user = mAuth.getCurrentUser();
        //uid = user.getUid();

        // initializing onclick listener for button.
        generateQrBtn.setOnClickListener(v -> {
            // Initialize variables
            lobbyMap = generateLobbyMap(state.getUid());

            // Add lobby
            state.getDb().collection("lobbies").add(lobbyMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        boolean active = true;
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            lobby = documentReference.getId();
                            Log.d(TAG, "DocumentSnapshot written with ID: " + lobby);
                            // Generate QR code from given string code
                            generateQRCode(lobby);
                            Toast.makeText(PairingModeActivity.this, "Generated lobby " + lobby, Toast.LENGTH_SHORT).show();
                            final DocumentReference docRef = state.getDb().collection("lobbies").document(lobby);
                            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        Log.w(TAG, "Listen failed.", e);
                                        return;
                                    }

                                    if (snapshot != null && snapshot.exists() && !snapshot.getData().get("playerBName").equals("")&&active) {
                                        //TODO: Deregister listener, instead of using active boolean
                                        Intent intent = new Intent(getApplicationContext(), TradeModeActivity.class);
                                        intent.putExtra("lobbyid", lobby);
                                        intent.putExtra("clientid", state.getUid());

                                        // Stop showing the QR code
                                        qrCodeIV.clearColorFilter();
                                        qrCodeIV.setImageDrawable(null);
                                        qrCodeIV.invalidate();

                                        startActivity(intent);
                                        active =false;
                                    }
                                }
                            });
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

    /**
     * Generate a HashMap for a new lobby.
     */
    private HashMap generateLobbyMap(String uid) {
        HashMap lobbyMap = new HashMap();

        // Add lobby data
        lobbyMap.put("playerAName", uid);
        lobbyMap.put("playerBName", "");

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
                DocumentReference docRef = state.getDb().collection("lobbies").document(lobby);
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Read data
                            lobbyMap = document.getData();

                            // Modify data
                            lobbyMap.put("playerBName", state.getUid());


                            // Write data
                            state.getDb().collection("lobbies").document(lobby)
                                    .set(lobbyMap)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(PairingModeActivity.this, "Logged in on " + lobby, Toast.LENGTH_SHORT).show();
                                        Log.d("WORRY", "DocumentSnapshot successfully written!");
                                        Intent intent = new Intent(getApplicationContext(), TradeModeActivity.class);
                                        intent.putExtra("lobbyid", lobby);
                                        intent.putExtra("clientid", state.getUid());
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> Log.w("WoRRY", "Error writing document", e));
                        } else {
                            Log.d(TAG, "No such document");
                            Toast.makeText(PairingModeActivity.this, "Expired Lobby", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
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
}