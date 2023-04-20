package com.example.cardhub.card_creation;

import android.net.Uri;

import com.example.cardhub.inventory.Card;
import com.example.cardhub.inventory.InventoryData;
import com.example.cardhub.inventory.InventoryRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import junit.framework.TestCase;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import kotlin.jvm.internal.unsafe.MonitorKt;

public class CardCreationDataTest extends TestCase {

    @Mock
    FirebaseAuth auth;
    @Mock
    FirebaseFirestore db;
    @Mock
    FirebaseStorage storage;

    CardCreationData data;

    @Captor
    ArgumentCaptor<OnCompleteListener> onCompleteListenerArgumentCaptor;

    final String SERVER_URL = "xxxxx";


    public CardCreationDataTest() {
        MockitoAnnotations.openMocks(this);
        this.data = new CardCreationData(db, auth, storage);
    }

    public void testPublishCard() {
        // Adjust Mock
        Card testCard = new Card("test name", "test desc", Card.Rarity.LEGENDARY, "test url");

        StorageReference storageReferenceMockRef = Mockito.mock(StorageReference.class);
        Mockito.when(storage.getReference("card_images/")).thenReturn(storageReferenceMockRef);

        StorageReference storageReferenceMockImageRef = Mockito.mock(StorageReference.class);

        Task downloadUrlTask = Mockito.mock(Task.class);
        Mockito.when(downloadUrlTask.isSuccessful()).thenReturn(true);

        Mockito.when(storageReferenceMockImageRef.getDownloadUrl()).thenReturn(downloadUrlTask);

        Mockito.when(storageReferenceMockRef.child(testCard.NAME + ".png")).thenReturn(storageReferenceMockImageRef);

        UploadTask uploadTaskMock = Mockito.mock(UploadTask.class);
        Mockito.when(uploadTaskMock.isSuccessful()).thenReturn(true);

        Uri uriMock = Mockito.mock(Uri.class);
        Mockito.when(uriMock.toString()).thenReturn(SERVER_URL);
        Mockito.when(downloadUrlTask.getResult()).thenReturn(uriMock);

        Mockito.when(storageReferenceMockImageRef.putFile(Uri.parse(testCard.IMAGE_URL))).thenReturn(uploadTaskMock);

        Mockito.when(uploadTaskMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(uploadTaskMock);
                    return null;
                }
        );

        Mockito.when(downloadUrlTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(downloadUrlTask);
                    return null;
                }
        );

        CollectionReference collectionReferenceMockCards = Mockito.mock(CollectionReference.class);

        Mockito.when(db.collection("cards/")).thenReturn(collectionReferenceMockCards);

        Task<DocumentReference> taskMockGetCards = Mockito.mock(Task.class);
        Mockito.when(taskMockGetCards.isSuccessful()).thenReturn(true);

        HashMap<String, Object> finalCard = (HashMap)testCard.serialize();
        finalCard.put("imageurl", SERVER_URL);

        Mockito.when(collectionReferenceMockCards.add(finalCard)).thenReturn(taskMockGetCards);

        Mockito.when(taskMockGetCards.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(taskMockGetCards);
                    return null;
                }
        );

        Mockito.when(auth.getUid()).thenReturn("x");

        CollectionReference collectionReferenceMockUserCards = Mockito.mock(CollectionReference.class);

        Mockito.when(db.collection("users/x/cards/")).thenReturn(collectionReferenceMockUserCards);

        Task<DocumentReference> taskMockGetUserCards = Mockito.mock(Task.class);
        Mockito.when(taskMockGetUserCards.isSuccessful()).thenReturn(true);
        Mockito.when(collectionReferenceMockUserCards.add(finalCard)).thenReturn(taskMockGetUserCards);

        Mockito.when(taskMockGetUserCards.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(taskMockGetUserCards);
                    return null;
                }
        );

        // Act
        data.publishCard(testCard);

        // Verify
        Mockito.verify(collectionReferenceMockCards).add(finalCard);
    }
}