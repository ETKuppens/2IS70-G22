package com.example.cardhub.map;

import com.example.cardhub.inventory.Card;
import com.example.cardhub.user_profile.ProfileData;
import com.example.cardhub.user_profile.ProfileRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import junit.framework.TestCase;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.listeners.MockitoListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapDataTest extends TestCase {
    @Mock
    FirebaseAuth auth;
    @Mock
    FirebaseFirestore db;
    MapData data;
    @Mock
    MapRepository repo;

    @Captor
    ArgumentCaptor<OnCompleteListener> onCompleteListenerArgumentCaptor;

    public MapDataTest() {
        MockitoAnnotations.openMocks(this);
        this.data = new MapData(repo, db, auth);
    }

    public void testRequestPacks() {
        // Arrange the mock
        CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("cardpacks/")).thenReturn(collectionReferenceMock);

        Task<QuerySnapshot> cardpacksTaskMock = Mockito.mock(Task.class);
        Mockito.when(cardpacksTaskMock.isSuccessful()).thenReturn(true);
        Mockito.when(collectionReferenceMock.get()).thenReturn(cardpacksTaskMock);

        Mockito.when(cardpacksTaskMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(cardpacksTaskMock);
                    return null;
                }
        );

        QuerySnapshot cardPacksSnapshot = Mockito.mock(QuerySnapshot.class);
        Mockito.when(cardpacksTaskMock.getResult()).thenReturn(cardPacksSnapshot);

        DocumentSnapshot documentSnapshotMock = Mockito.mock(DocumentSnapshot.class);

        HashMap<String, Object> documentData = new HashMap<>();
        documentData.put("example data", 5);

        Mockito.when(documentSnapshotMock.getData()).thenReturn(documentData);

        List<DocumentSnapshot> documentSnapshots = Collections.singletonList(documentSnapshotMock);

        Mockito.when(cardPacksSnapshot.getDocuments()).thenReturn(documentSnapshots);

        // Act
        data.requestPacks();

        // Verify
        List<Map<String, Object>> expected = Collections.singletonList(documentData);

        Mockito.verify(repo).receivePacks(expected);
    }

    public void testAcquireRandomCard() {
        // Arrange mock
        CollectionReference cardsCollectionReferenceMock = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("cards/")).thenReturn(cardsCollectionReferenceMock);

        Task<QuerySnapshot> cardsTaskMock = Mockito.mock(Task.class);
        Mockito.when(cardsTaskMock.isSuccessful()).thenReturn(true);
        Mockito.when(cardsCollectionReferenceMock.get()).thenReturn(cardsTaskMock);

        Mockito.when(cardsTaskMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(cardsTaskMock);
                    return null;
                }
        );

        QuerySnapshot cardsQuerySnapshotMock = Mockito.mock(QuerySnapshot.class);
        Mockito.when(cardsTaskMock.getResult()).thenReturn(cardsQuerySnapshotMock);

        DocumentSnapshot correctCardSnapshot = Mockito.mock(DocumentSnapshot.class);
        Map<String,Object> correctCardData = new HashMap<>();
        correctCardData.put("rarity", "LEGENDARY");
        correctCardData.put("name", "Unit test card");
        correctCardData.put("description", "Unit test card desc");
        correctCardData.put("imageurl", "Unit test card url");
        Mockito.when(correctCardSnapshot.getData()).thenReturn(correctCardData);

        DocumentSnapshot wrongCardSnapshot = Mockito.mock(DocumentSnapshot.class);
        Map<String,Object> wrongCardData = new HashMap<>();
        wrongCardData.put("rarity", "COMMON");
        Mockito.when(wrongCardSnapshot.getData()).thenReturn(wrongCardData);

        List<DocumentSnapshot> cardsDocumentSnapshotMocks = Arrays.asList(
            correctCardSnapshot, wrongCardSnapshot
        );
        Mockito.when(cardsQuerySnapshotMock.getDocuments()).thenReturn(cardsDocumentSnapshotMocks);

        Mockito.when(auth.getUid()).thenReturn("x");

        CollectionReference mockCollectionRef = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("users/x/cards")).thenReturn(mockCollectionRef);

        Task<DocumentReference> cardAddTaskMock = Mockito.mock(Task.class);
        Mockito.when(cardAddTaskMock.isSuccessful()).thenReturn(true);

        Mockito.when(db.collection("users/x/cards").add(correctCardData))
                .thenReturn(cardAddTaskMock);

        Mockito.when(cardAddTaskMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).
                thenAnswer((obj) ->
                {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(cardAddTaskMock);
                    return null;
                });

        // Act
        data.acquireRandomCard(Card.Rarity.LEGENDARY);

        // Verify
        Mockito.verify(repo).acquireRandomCardCallback(correctCardData);
    }
}