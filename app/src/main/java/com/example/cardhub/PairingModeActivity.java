package com.example.cardhub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PairingModeActivity extends AppCompatActivity {
    private ImageView qrCodeIV;
    private EditText dataEdt;

    private TextView dataReceived;
    private Button generateQrBtn;

    private Button scanQrBtn;

    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairingmode);

        // initializing all variables.
        qrCodeIV = findViewById(R.id.idIVQrcode);
        dataEdt = findViewById(R.id.idEdt);
        dataReceived = findViewById(R.id.idDisplay);
        generateQrBtn = findViewById(R.id.idBtnGenerateQR);
        scanQrBtn = findViewById(R.id.idScanQrCode);

        // initializing onclick listener for button.
        generateQrBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(dataEdt.getText().toString())) {

                // if the edittext inputs are empty then execute
                // this method showing a toast message.
                Toast.makeText(PairingModeActivity.this, "Enter some text to generate QR Code", Toast.LENGTH_SHORT).show();
            } else {
                // below line is for getting
                // the windowmanager service.
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
                qrgEncoder = new QRGEncoder(dataEdt.getText().toString(), QRGContents.Type.TEXT, dimen);
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
        });

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
                // if the intentResult is not null we'll set
                // the content and format of scan message
                dataReceived.setText(intentResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}