package com.example.cardhub.inventory;

import com.example.cardhub.inventory.InventoryData;
import com.example.cardhub.inventory.InventoryRepository;
import com.example.cardhub.map.MapData;
import com.example.cardhub.map.MapRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import junit.framework.TestCase;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InventoryDataTest extends TestCase {
    @Mock
    FirebaseAuth auth;
    @Mock
    FirebaseFirestore db;
    InventoryData data;
    @Mock
    InventoryRepository repo;

    @Captor
    ArgumentCaptor<OnCompleteListener> onCompleteListenerArgumentCaptor;


    public InventoryDataTest() {
        MockitoAnnotations.openMocks(this);
        this.data = new InventoryData(repo, auth, db);
    }

    public void testRequestAllCards() {
        // Arrange Mock
        CollectionReference mockCollectionRef = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("cards/")).thenReturn(mockCollectionRef);

        Task<QuerySnapshot> qsResult = Mockito.mock(Task.class);
        Mockito.when(qsResult.isSuccessful()).thenReturn(true);

        QuerySnapshot querySnapshotMock = Mockito.mock(QuerySnapshot.class);

        QueryDocumentSnapshot queryDocumentSnapshotCard1 = Mockito.mock(QueryDocumentSnapshot.class);
        Map<String,Object> card1Data = new HashMap<>();
        card1Data.put("name", "test1");
        Mockito.when(queryDocumentSnapshotCard1.getData()).thenReturn(card1Data);

        QueryDocumentSnapshot queryDocumentSnapshotCard2 = Mockito.mock(QueryDocumentSnapshot.class);
        Map<String,Object> card2Data = new HashMap<>();
        card1Data.put("name", "test2");
        Mockito.when(queryDocumentSnapshotCard2.getData()).thenReturn(card2Data);

        List<QueryDocumentSnapshot> documentSnapshotsExt = Arrays.asList(
                queryDocumentSnapshotCard1,
                queryDocumentSnapshotCard2
        );

        Mockito.when(querySnapshotMock.iterator()).thenReturn(new Iterator<QueryDocumentSnapshot>() {
            int index = 0;

            final List<QueryDocumentSnapshot> documentSnapshots = documentSnapshotsExt;

            @Override
            public boolean hasNext() {
                return index < documentSnapshots.size();
            }

            @Override
            public QueryDocumentSnapshot next() {
                QueryDocumentSnapshot val = documentSnapshots.get(index);
                index++;
                return val;
            }
        });
        Mockito.when(qsResult.getResult()).thenReturn(querySnapshotMock);

        Mockito.when(db.collection("cards/").get()).thenReturn(qsResult);
        Mockito.when(db.collection("cards/").get().
                        addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).
                thenAnswer((obj) ->
                {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(qsResult);
                    return null;
                });

        // Act
        data.requestAllCards();

        // Verify
        List<Map<String, Object>> expected = Arrays.asList(
                card1Data,
                card2Data
        );
        Mockito.verify(repo).cardRequestCallback(expected);

    }

    public void testRequestUserCards() {
        // Arrange Mock
        Mockito.when(auth.getUid()).thenReturn("x");

        CollectionReference mockCollectionRef = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("users/x/cards")).thenReturn(mockCollectionRef);

        Task<QuerySnapshot> qsResult = Mockito.mock(Task.class);
        Mockito.when(qsResult.isSuccessful()).thenReturn(true);

        QuerySnapshot querySnapshotMock = Mockito.mock(QuerySnapshot.class);

        QueryDocumentSnapshot queryDocumentSnapshotCard1 = Mockito.mock(QueryDocumentSnapshot.class);
        Map<String,Object> card1Data = new HashMap<>();
        card1Data.put("name", "test1");
        Mockito.when(queryDocumentSnapshotCard1.getData()).thenReturn(card1Data);

        QueryDocumentSnapshot queryDocumentSnapshotCard2 = Mockito.mock(QueryDocumentSnapshot.class);
        Map<String,Object> card2Data = new HashMap<>();
        card1Data.put("name", "test2");
        Mockito.when(queryDocumentSnapshotCard2.getData()).thenReturn(card2Data);

        List<QueryDocumentSnapshot> documentSnapshotsExt = Arrays.asList(
                queryDocumentSnapshotCard1,
                queryDocumentSnapshotCard2
        );

        Mockito.when(querySnapshotMock.iterator()).thenReturn(new Iterator<QueryDocumentSnapshot>() {
            int index = 0;

            final List<QueryDocumentSnapshot> documentSnapshots = documentSnapshotsExt;

            @Override
            public boolean hasNext() {
                return index < documentSnapshots.size();
            }

            @Override
            public QueryDocumentSnapshot next() {
                QueryDocumentSnapshot val = documentSnapshots.get(index);
                index++;
                return val;
            }
        });
        Mockito.when(qsResult.getResult()).thenReturn(querySnapshotMock);

        Mockito.when(db.collection("users/x/cards").get()).thenReturn(qsResult);
        Mockito.when(db.collection("users/x/cards").get().
                        addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).
                thenAnswer((obj) ->
                {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(qsResult);
                    return null;
                });

        // Act
        data.requestUserCards();

        // Verify
        List<Map<String, Object>> expected = Arrays.asList(
                card1Data,
                card2Data
        );
        Mockito.verify(repo).cardRequestCallback(expected);

    }
}