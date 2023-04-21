package com.example.cardhub.PairingMode;

import android.graphics.Bitmap;
import android.view.Display;
import android.view.WindowManager;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PairingModeStateTest {

    @Test
    public void generateBitmapTest(){
        PairingModeState state = new PairingModeState();

        WindowManager manager = Mockito.mock(WindowManager.class);

        Display display = Mockito.mock(Display.class);
        Mockito.when(manager.getDefaultDisplay()).thenReturn(display);

        Bitmap actual = state.generateBitmap("test", manager);

        QRGEncoder qrgEncoder = new QRGEncoder("test", QRGContents.Type.TEXT, 0);

        assert(actual == qrgEncoder.getBitmap());


    }
}
