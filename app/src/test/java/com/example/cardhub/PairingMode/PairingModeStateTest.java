package com.example.cardhub.PairingMode;

import android.graphics.Bitmap;
import android.view.WindowManager;

import org.junit.Test;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PairingModeStateTest {

    @Test
    public void generateBitmapTest(){
        PairingModeState state = new PairingModeState();

        Bitmap actual = state.generateBitmap("test", null);

        QRGEncoder qrgEncoder = new QRGEncoder("test", QRGContents.Type.TEXT, 0);

        assert(actual == qrgEncoder.getBitmap());


    }
}
