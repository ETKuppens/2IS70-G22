package com.example.cardhub.PairingMode;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import junit.framework.TestCase;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoInitializationException;

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
        WindowManager manager = Mockito.mock(WindowManager.class);

        QRGEncoder qrgEncoder = new QRGEncoder(code, QRGContents.Type.TEXT, 0);
        // getting our qrcode in the form of bitmap.
        Bitmap bitmap = qrgEncoder.getBitmap();

        Display displayMock = Mockito.mock(Display.class);
        Mockito.when(manager.getDefaultDisplay()).thenReturn(displayMock);

        // Act
        Bitmap result = data.generateBitmap(code, manager);

        // Assert
        assertEquals(bitmap, result);
    }
}
