package com.example.cardhub.PairingMode;

import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class PairingModeRepositoryImplTest extends TestCase {
    @Mock
    PairingModeData data;
    @Mock
    PairingModeRepositoryReceiver receiver;
    PairingModeRepositoryImpl repo;

    public PairingModeRepositoryImplTest() {
        MockitoAnnotations.openMocks(this);
        this.repo = new PairingModeRepositoryImpl(receiver, data);
    }

    public void testGetUid() {
        repo.getUid();

        Mockito.verify(data).getUid();
    }

    public void testGenerateQR() {
        repo.generateQR("test");

        Mockito.verify(receiver).generateQR("test");
    }

    public void testLobbyCreated() {
        repo.lobbyCreated("test");

        Mockito.verify(receiver).lobbyCreated("test");
    }

    public void testJoinedLobby() {
        repo.joinedLobby("test");

        Mockito.verify(receiver).joinedLobby("test");
    }

    public void testGenerateLobby() {
        repo.generateLobby();

        Mockito.verify(data).generateLobby();
    }

    public void testJoinLobby() {
        repo.joinLobby("test");

        Mockito.verify(data).joinLobby("test");
    }
}
