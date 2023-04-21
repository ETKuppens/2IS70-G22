package com.example.cardhub.PairingMode;

public interface PairingModeRepository {

    /**
     * Get the current user's uid.
     *
     * @return
     */
    String getUid();

    /**
     * Generates the QR code of the lobby id.
     *
     * @param lobby the lobby id
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

    /**
     * Generates a lobby id.
     */
    void generateLobby();

    /**
     * Joins a lobby.
     *
     * @param lobby the lobby id
     */
    void joinLobby(String lobby);
}
