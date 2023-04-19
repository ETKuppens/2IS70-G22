package com.example.cardhub.TradingMode;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

import com.example.cardhub.card_creation.CardCreationData;
import com.example.cardhub.inventory.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firestore.v1.CreateDocumentRequest;
import com.google.firestore.v1.Write;

import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TradingSessionDataTest extends TestCase {

    @Mock
    FirebaseFirestore db;
    @Mock
    TradingSessionRepository repo;

    TradingSessionData data;
    @Mock
    DocumentReference docRefMock;
    @Mock
    ListenerRegistration listenerRegistration;

    @Captor
    ArgumentCaptor<OnCompleteListener> onCompleteListenerArgumentCaptor;
    @Captor
    ArgumentCaptor<OnSuccessListener> onSuccessListenerArgumentCaptor;
    @Captor
    ArgumentCaptor<OnFailureListener> onFailureListenerArgumentCaptor;
    @Captor
    ArgumentCaptor<EventListener> onEventListenerArgumentCaptor;

    final String LOBBY_ID ="x";
    final String CLIENT_ID = "x";
    final String OTHER_PLAYER = "playerB";
    final String CURRENT_PLAYER = "playerA";
    final String PLAYER_A_NAME ="a";
    final String PLAYER_B_NAME ="b";

    public TradingSessionDataTest() {
        MockitoAnnotations.openMocks(this);

        CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("lobbies")).thenReturn(collectionReferenceMock);
        Mockito.when(collectionReferenceMock.document(LOBBY_ID)).thenReturn(docRefMock);

        setupGetInfo();

        data = new TradingSessionData(repo, db, LOBBY_ID, CLIENT_ID);
    }

    private void setupGetInfo() {
        Task taskMock = Mockito.mock(Task.class);

        Mockito.when(docRefMock.get()).thenReturn(taskMock);

        Mockito.when(taskMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(taskMock);
                    return null;
                }
        );

        Mockito.when(taskMock.isSuccessful()).thenReturn(true);

        DocumentSnapshot documentSnapshotMock = Mockito.mock(DocumentSnapshot.class);
        Mockito.when(taskMock.getResult()).thenReturn(documentSnapshotMock);

        Mockito.when(documentSnapshotMock.exists()).thenReturn(true);

        Mockito.when(documentSnapshotMock.getString("playerAName")).thenReturn(CLIENT_ID);
        Mockito.when(documentSnapshotMock.getString("playerBName")).thenReturn("some id");

        CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
        Mockito.when(docRefMock.collection("cardDiffs_" + OTHER_PLAYER)).thenReturn(collectionReferenceMock);

        QuerySnapshot querySnapshotMock = Mockito.mock(QuerySnapshot.class);

        Mockito.when(collectionReferenceMock.addSnapshotListener(onEventListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    //onEventListenerArgumentCaptor.getValue().onEvent(querySnapshotMock, null);
                    return null;
                }
        );

        Mockito.when(docRefMock.addSnapshotListener(onEventListenerArgumentCaptor.capture())).thenReturn(listenerRegistration);
    }

    public void testCancelTradingSession() {
        // Arrange mock
        Task taskMock = Mockito.mock(Task.class);
        Mockito.when(taskMock.isSuccessful()).thenReturn(true);
        Mockito.when(docRefMock.delete()).thenReturn(taskMock);

        Mockito.when(taskMock.addOnSuccessListener(onSuccessListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onSuccessListenerArgumentCaptor.getValue().onSuccess(null);
                    return taskMock;
                }
        );
        // Act
        data.cancelTradingSession(CLIENT_ID);

        // Verify
        Mockito.verify(repo).cancelTradingSessionConfirm(CLIENT_ID);
    }

    public void testAcceptProposedTrade() {
        // Adjust mock
        Map<String, Object> expected = new HashMap<>();
        expected.put("acceptance_" + CURRENT_PLAYER, true);

        Task taskMock = Mockito.mock(Task.class);
        Mockito.when(docRefMock.set(expected, SetOptions.merge())).thenReturn(taskMock);
        Mockito.when(taskMock.addOnSuccessListener(any())).thenReturn(taskMock);

        // Act
        data.acceptProposedTrade();

        // Verify
        Mockito.verify(docRefMock).set(expected, SetOptions.merge());
    }

    public void testCancelAcceptTrade() {
        // Adjust mock
        Map<String, Object> expected = new HashMap<>();
        expected.put("acceptance_" + CURRENT_PLAYER, false);

        Task taskMock = Mockito.mock(Task.class);
        Mockito.when(docRefMock.set(expected, SetOptions.merge())).thenReturn(taskMock);
        Mockito.when(taskMock.addOnSuccessListener(any())).thenReturn(taskMock);

        // Act
        data.cancelAcceptTrade();

        // Verify
        Mockito.verify(docRefMock).set(expected, SetOptions.merge());
    }

    public void testChangeProposedCards() {
        // Adjust mock
        Set<CardDiff> cardDiffs = new HashSet<>();
        CardDiff diff =
                new CardDiff(
                        new Card(
                                "test name",
                                "test desc",
                                Card.Rarity.LEGENDARY,
                                "test url"),
                        CardDiff.DiffOption.ADD
                );
        cardDiffs.add(diff);

        WriteBatch batchMock = Mockito.mock(WriteBatch.class);
        Mockito.when(db.batch()).thenReturn(batchMock);

        CollectionReference collectionReference = Mockito.mock(CollectionReference.class);
        Mockito.when(docRefMock.collection("cardDiffs_" + CURRENT_PLAYER)).thenReturn(
                collectionReference
        );

        DocumentReference documentReferenceMock = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReference.document()).thenReturn(documentReferenceMock);


        Task taskMock = Mockito.mock(Task.class);

        Mockito.when(batchMock.commit()).thenReturn(taskMock);

        Mockito.when(taskMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture()))
                .thenAnswer(
                        (obj) -> {
                            onCompleteListenerArgumentCaptor.getValue().onComplete(taskMock);
                            return null;
                        }
                );

        Mockito.when(taskMock.isSuccessful()).thenReturn(true);

        // Act
        data.changeProposedCards(CLIENT_ID, cardDiffs);

        // Verify
        Mockito.verify(repo).changeProposedCardsConfirm(CLIENT_ID);
        Mockito.verify(batchMock).set(documentReferenceMock, diff.serialize());
    }

    public void testDoTrade() {
        // Adjust Mock
        CollectionReference collectionReferenceMockA = Mockito.mock(CollectionReference.class);
        Mockito.when(docRefMock.collection("cardDiffs_" + CURRENT_PLAYER)).thenReturn(collectionReferenceMockA);

        Task taskMockA = Mockito.mock(Task.class);
        Mockito.when(collectionReferenceMockA.get()).thenReturn(taskMockA);

        Mockito.when(taskMockA.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(taskMockA);
                    return null;
                }
        );

        Mockito.when(taskMockA.isSuccessful()).thenReturn(true);

        QuerySnapshot querySnapshotMock = Mockito.mock(QuerySnapshot.class);
        Mockito.when(taskMockA.getResult()).thenReturn(querySnapshotMock);

        QueryDocumentSnapshot queryDocumentSnapshotMock = Mockito.mock(QueryDocumentSnapshot.class);

        Mockito.when(querySnapshotMock.iterator()).thenReturn(new Iterator<QueryDocumentSnapshot>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < 1;
            }

            @Override
            public QueryDocumentSnapshot next() {
                index++;
                return queryDocumentSnapshotMock;
            }
        });

        final String OFFERED_A_NAME="player A card";
        final String OFFERED_B_NAME="player B card";

        Map<String, Object> playerACardDiff = new HashMap<>();
        Map<String, Object> playerACardDiffCard = new HashMap<>();
        playerACardDiffCard.put("name", OFFERED_A_NAME);
        playerACardDiff.put("card", playerACardDiffCard);

        Mockito.when(queryDocumentSnapshotMock.getData()).thenReturn(playerACardDiff);

        // OTHER PLAYER (PLAYER B)
        CollectionReference collectionReferenceMockB = Mockito.mock(CollectionReference.class);

        Mockito.when(docRefMock.collection("cardDiffs_" + OTHER_PLAYER)).thenReturn(collectionReferenceMockB);

        Task taskMockB = Mockito.mock(Task.class);
        Mockito.when(collectionReferenceMockB.get()).thenReturn(taskMockB);

        Mockito.when(taskMockB.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(taskMockB);
                    return null;
                }
        );

        Mockito.when(taskMockB.isSuccessful()).thenReturn(true);

        QuerySnapshot querySnapshotMockB = Mockito.mock(QuerySnapshot.class);
        Mockito.when(taskMockB.getResult()).thenReturn(querySnapshotMockB);

        QueryDocumentSnapshot queryDocumentSnapshotMockB = Mockito.mock(QueryDocumentSnapshot.class);

        Mockito.when(querySnapshotMockB.iterator()).thenReturn(new Iterator<QueryDocumentSnapshot>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < 1;
            }

            @Override
            public QueryDocumentSnapshot next() {
                index++;
                return queryDocumentSnapshotMockB;
            }
        });

        Map<String, Object> playerBCardDiff = new HashMap<>();
        Map<String, Object> playerBCardDiffCard = new HashMap<>();
        playerBCardDiffCard.put("name", OFFERED_B_NAME);
        playerBCardDiff.put("card", playerBCardDiffCard);

        Mockito.when(queryDocumentSnapshotMockB.getData()).thenReturn(playerBCardDiff);

        //===========================================
        Task taskMockGet = Mockito.mock(Task.class);
        Mockito.when(docRefMock.get()).thenReturn(taskMockGet);

        Mockito.when(taskMockGet.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(taskMockGet);
                    return null;
                }
        );

        Mockito.when(taskMockGet.isSuccessful()).thenReturn(true);

        DocumentSnapshot playerNameSnapshotMock = Mockito.mock(DocumentSnapshot.class);
        Mockito.when(playerNameSnapshotMock.get("playerAName")).thenReturn(PLAYER_A_NAME);
        Mockito.when(playerNameSnapshotMock.get("playerBName")).thenReturn(PLAYER_B_NAME);

        Mockito.when(taskMockGet.getResult()).thenReturn(playerNameSnapshotMock);

        WriteBatch writeBatchMock = Mockito.mock(WriteBatch.class);
        Mockito.when(db.batch()).thenReturn(writeBatchMock);

        CollectionReference playerACardsMock = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("users/" + PLAYER_A_NAME + "/cards")).thenReturn(playerACardsMock);

        Query queryMock = Mockito.mock(Query.class);
        Mockito.when(playerACardsMock.whereIn("name", Arrays.asList(OFFERED_A_NAME, "DO_NOT_USE_THIS_NAME"))).thenReturn(queryMock);

        Task queryReturnTaskMock = Mockito.mock(Task.class);
        Mockito.when(queryMock.get()).thenReturn(queryReturnTaskMock);

        Mockito.when(queryReturnTaskMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(queryReturnTaskMock);
                    return null;
                }
        );

        Mockito.when(queryReturnTaskMock.isSuccessful()).thenReturn(true);

        QuerySnapshot querySnapshotMockQuery = Mockito.mock(QuerySnapshot.class);
        Mockito.when(queryReturnTaskMock.getResult()).thenReturn(querySnapshotMockQuery);

        QueryDocumentSnapshot queryDocumentSnapshotMockQuery = Mockito.mock(QueryDocumentSnapshot.class);
        Mockito.when(querySnapshotMockQuery.iterator()).thenReturn(new Iterator<QueryDocumentSnapshot>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < 1;
            }

            @Override
            public QueryDocumentSnapshot next() {
                index++;
                return queryDocumentSnapshotMockQuery;
            }
        });

        DocumentReference toDeleteAInventoryReference = Mockito.mock(DocumentReference.class);
        Mockito.when(queryDocumentSnapshotMockQuery.getReference()).thenReturn(toDeleteAInventoryReference);

        // removed traded cards from player a inventory

        CollectionReference playerBCardsMock = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("users/" + PLAYER_B_NAME + "/cards")).thenReturn(playerBCardsMock);

        Query queryMockB = Mockito.mock(Query.class);
        Mockito.when(playerBCardsMock.whereIn("name", Arrays.asList(OFFERED_B_NAME, "DO_NOT_USE_THIS_NAME"))).thenReturn(queryMockB);

        Task queryReturnTaskMockB = Mockito.mock(Task.class);
        Mockito.when(queryMockB.get()).thenReturn(queryReturnTaskMockB);

        Mockito.when(queryReturnTaskMockB.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(queryReturnTaskMockB);
                    return null;
                }
        );

        Mockito.when(queryReturnTaskMockB.isSuccessful()).thenReturn(true);

        QuerySnapshot querySnapshotMockQueryB = Mockito.mock(QuerySnapshot.class);
        Mockito.when(queryReturnTaskMockB.getResult()).thenReturn(querySnapshotMockQueryB);

        QueryDocumentSnapshot queryDocumentSnapshotMockQueryB = Mockito.mock(QueryDocumentSnapshot.class);
        Mockito.when(querySnapshotMockQueryB.iterator()).thenReturn(new Iterator<QueryDocumentSnapshot>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < 1;
            }

            @Override
            public QueryDocumentSnapshot next() {
                index++;
                return queryDocumentSnapshotMockQueryB;
            }
        });

        DocumentReference toDeleteBInventoryReference = Mockito.mock(DocumentReference.class);
        Mockito.when(queryDocumentSnapshotMockQueryB.getReference()).thenReturn(toDeleteBInventoryReference);

        // deleted offered cards from b inventory

        // test for exchange cards
        DocumentReference refInvA = Mockito.mock(DocumentReference.class);
        Mockito.when(playerACardsMock.document()).thenReturn(refInvA);

        DocumentReference refInvB = Mockito.mock(DocumentReference.class);
        Mockito.when(playerBCardsMock.document()).thenReturn(refInvB);

        Task batchCommitTaskMock = Mockito.mock(Task.class);
        Mockito.when(writeBatchMock.commit()).thenReturn(batchCommitTaskMock);

        Mockito.when(batchCommitTaskMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(batchCommitTaskMock);
                    return null;
                }
        );

        Mockito.when(batchCommitTaskMock.isSuccessful()).thenReturn(true);

        CollectionReference collectionReferenceMockUsers = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("users")).thenReturn(collectionReferenceMockUsers);

        DocumentReference userADocRef = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReferenceMockUsers.document(PLAYER_A_NAME)).thenReturn(userADocRef);

        DocumentReference userBDocRef = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReferenceMockUsers.document(PLAYER_B_NAME)).thenReturn(userBDocRef);


        // Act
        data.doTrade();

        // Verify
        // verify that cards are removed from respective inventories
        Mockito.verify(writeBatchMock).delete(toDeleteAInventoryReference);
        Mockito.verify(writeBatchMock).delete(toDeleteBInventoryReference);

        // verify that cards are added to respective inventories
        Mockito.verify(writeBatchMock).set(refInvA, playerBCardDiffCard);
        Mockito.verify(writeBatchMock).set(refInvB, playerACardDiffCard);

        // verify that the lobby is finished
        Mockito.verify(docRefMock).update("finished", true);

        // verify that listener is removed
        Mockito.verify(listenerRegistration).remove();

        // verify that the repository is alerted
        Mockito.verify(repo).finishTrade();
    }
}