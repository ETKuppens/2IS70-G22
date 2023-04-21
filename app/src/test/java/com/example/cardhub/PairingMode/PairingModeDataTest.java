package com.example.cardhub.PairingMode;

import android.graphics.Bitmap;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import junit.framework.TestCase;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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

    public PairingModeDataTest() {
        MockitoAnnotations.openMocks(this);
        data = new PairingModeData(repo, auth, db);
    }

    public void testGetUid() {
        // Arrange Mock
        String uid = "test";
        Mockito.when(auth.getUid()).thenReturn(uid);

        // Act
        String result = data.getUid();

        // Assert
        assertEquals(uid, result);
    }

    //public void testGetDb() {
    //    // Arrange Mock
    //    FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);
    //    Mockito.when(this.db).thenReturn(db);
//
//        Task<QuerySnapshot> dbResult = Mockito.mock(Task.class);
//        Mockito.when(dbResult.isSuccessful()).thenReturn(true);
//
//        QuerySnapshot SnapshotMock = Mockito.mock(QuerySnapshot.class);
//
//        QueryDocumentSnapshot queryDocumentSnapshotCard1 = Mockito.mock(QueryDocumentSnapshot.class);
//
//        Mockito.when(SnapshotMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
//                (obj) -> {
//                    onCompleteListenerArgumentCaptor.getValue().onComplete(SnapshotMock);
//                    return null;
//                }
//        );
//
//        // Act
//        FirebaseFirestore result = data.getDb();
//
//        // Assert
//        assertEquals(db, result);
//    }

    public void testGenerateBitmap() {
        // Arrange Mock
        String code = "test";
        PairingModeRepositoryReceiver receiver = Mockito.mock(PairingModeRepositoryReceiver.class);
        WindowManager manager = Mockito.mock(WindowManager.class);
        Bitmap bitmap = Mockito.mock(Bitmap.class);
        Mockito.when(receiver.generateBitmap(code, manager)).thenReturn(bitmap);

        // Act
        Bitmap result = data.generateBitmap(code, manager);

        // Assert
        assertEquals(bitmap, result);
    }
}
