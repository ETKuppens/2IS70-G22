package com.example.cardhub.PairingMode;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoInitializationException;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PairingModeDataTest extends TestCase {
    @Mock
    FirebaseAuth auth;
    @Mock
    FirebaseFirestore db;

    PairingModeData data;

    @Mock
    PairingModeRepository repo;

    @Captor
    ArgumentCaptor<OnCompleteListener> onCompleteListenerArgumentCaptor;
    @Captor
    ArgumentCaptor<OnSuccessListener> onSuccessListenerArgumentCaptor;
    @Captor
    ArgumentCaptor<EventListener> onEventListenerArgumentCaptor;
    @Captor
    ArgumentCaptor<Point> pointCaptor;

    public PairingModeDataTest() {
        MockitoAnnotations.openMocks(this);
        data = new PairingModeData(repo, auth, db);
    }

    public void testGetUid() {
        // Arrange Mock
        String uid = "test";
        FirebaseUser firebaseUserMock = Mockito.mock(FirebaseUser.class);
        Mockito.when(auth.getCurrentUser()).thenReturn(firebaseUserMock);

        Mockito.when(firebaseUserMock.getUid()).thenReturn(uid);

        // Act
        String result = data.getUid();

        // Assert
        assertEquals(uid, result);
    }

    private HashMap<String,String> generateLobbyMap(String uid) {
        HashMap<String,String> lobbyMap = new HashMap<>();

        // Add lobby data
        lobbyMap.put("playerAName", uid);
        lobbyMap.put("playerBName", "");

        return lobbyMap;
    }

    public void testGenerateLobby() {
        final String UID = "x";
        Mockito.when(auth.getUid()).thenReturn(UID);

        CollectionReference lobbiesReference = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("lobbies")).thenReturn(lobbiesReference);

        HashMap<String, String> lobbyMap = generateLobbyMap(UID);

        Task addUIDTask = Mockito.mock(Task.class);
        Mockito.when(lobbiesReference.add(lobbyMap)).thenReturn(addUIDTask);

        DocumentReference docRefMock = Mockito.mock(DocumentReference.class);

        Mockito.when(addUIDTask.addOnSuccessListener(onSuccessListenerArgumentCaptor.capture())).thenAnswer(
                (obj) ->  {
                    onSuccessListenerArgumentCaptor.getValue().onSuccess(docRefMock);
                    return addUIDTask;
                }
        );

        final String docId = "xx";
        Mockito.when(docRefMock.getId()).thenReturn(docId);

        DocumentReference lobbyRef = Mockito.mock(DocumentReference.class);
        Mockito.when(lobbiesReference.document(docId)).thenReturn(lobbyRef);

        DocumentSnapshot snapshotMock = Mockito.mock(DocumentSnapshot.class);

        Mockito.when(lobbyRef.addSnapshotListener(onEventListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onEventListenerArgumentCaptor.getValue().onEvent(snapshotMock, null);
                    return null;
                }
        );

        Mockito.when(snapshotMock.exists()).thenReturn(true);

        Map<String, Object> result = new HashMap<>();
        result.put("playerBName", "x");

        Mockito.when(snapshotMock.getData()).thenReturn(result);

        // Act
        data.generateLobby();

        // Validate
        Mockito.verify(repo).generateQR(docId);
        Mockito.verify(repo).lobbyCreated(docId);
    }

    public void testJoinLobby() {
        final String UID = "x";
        final String LOBBYID = "xx";

        CollectionReference lobbiesReference = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("lobbies")).thenReturn(lobbiesReference);

        DocumentReference documentReferenceMock = Mockito.mock(DocumentReference.class);
        Mockito.when(lobbiesReference.document(LOBBYID)).thenReturn(documentReferenceMock);

        Task taskMock = Mockito.mock(Task.class);
        Mockito.when(documentReferenceMock.get()).thenReturn(taskMock);

        Mockito.when(taskMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (org) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(taskMock);
                    return null;
                }
        );
        Mockito.when(taskMock.isSuccessful()).thenReturn(true);

        DocumentSnapshot documentSnapshotMock = Mockito.mock(DocumentSnapshot.class);
        Mockito.when(taskMock.getResult()).thenReturn(documentSnapshotMock);

        Mockito.when(documentSnapshotMock.exists()).thenReturn(true);

        Map<String, Object> result = new HashMap<>();
        Mockito.when(documentSnapshotMock.getData()).thenReturn(result);

        Mockito.when(auth.getUid()).thenReturn(UID);

        Task addTaskMock = Mockito.mock(Task.class);
        Mockito.when(documentReferenceMock.set(result)).thenReturn(addTaskMock);

        Mockito.when(addTaskMock.addOnSuccessListener(onSuccessListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onSuccessListenerArgumentCaptor.getValue().onSuccess(null);
                    return addTaskMock;
                }
        );

        // Act
        data.joinLobby(LOBBYID);

        // Verify
        Mockito.verify(repo).joinedLobby(LOBBYID);

    }

}
