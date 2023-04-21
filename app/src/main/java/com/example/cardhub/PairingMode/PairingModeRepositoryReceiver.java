package com.example.cardhub.PairingMode;

import android.graphics.Bitmap;
import android.view.WindowManager;

public interface PairingModeRepositoryReceiver {

    /**
     * Get the current user's uid.
     *
     * @return The current user's uid.
     */
     String getUid();

    /**
     * Generates the bitmap for the QR code.
     *
     * @param code the code be encoded in the QR code
     * @param manager
     * @return  the bitmap for the QR code
     */
    Bitmap generateBitmap(String code, WindowManager manager);

    /**
     * Generates the QR code of the lobby id.
     *
     * @param lobby
     */
    void generateQR(String lobby);

    /**
     * Checks if lobby with lobby id was created.
     *
     * @param lobby the lobby id
     */
    void lobbyCreated(String lobby);

    /**
     * Checks if user joined a lobby with lobby id.
     *
     * @param lobby the lobby id
     */
    void joinedLobby(String lobby);
}
