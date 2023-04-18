
package com.example.cardhub.user_profile;

import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.ArgumentMatchers.notNull;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.cardhub.inventory.Card;
import com.google.firebase.firestore.QuerySnapshot;

import junit.framework.TestCase;

import org.jetbrains.annotations.NotNull;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class ProfileDataTest extends TestCase {
    @Mock
    FirebaseAuth auth;
    @Mock
    FirebaseFirestore db;
    ProfileData data;
    @Mock
    ProfileRepository repo;

    @Captor
    ArgumentCaptor<OnCompleteListener> onCompleteListenerArgumentCaptor;

    public ProfileDataTest() {
        MockitoAnnotations.openMocks(this);
        this.data = new ProfileData(repo, auth, db);
    }

    public void testLogOut() {
        Mockito.when(auth.getCurrentUser()).thenReturn(Mockito.mock(FirebaseUser.class));

        data.logOut();
        Mockito.verify(auth).signOut();
    }

    public void testRequestProfile() {
        Mockito.when(auth.getUid()).thenReturn("x");
        CollectionReference mockCollectionRef = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("users/x/cards")).thenReturn(mockCollectionRef);
        Task<QuerySnapshot> qsResult = Mockito.mock(Task.class);
        Mockito.when(qsResult.isSuccessful()).thenReturn(true);
        QuerySnapshot querySnapshotMock = Mockito.mock(QuerySnapshot.class);
        Mockito.when(qsResult.getResult()).thenReturn(querySnapshotMock);
        // card size is 2
        Mockito.when(querySnapshotMock.size()).thenReturn(2);

        Mockito.when(db.collection("users/x/cards").get()).thenReturn(qsResult);
        Mockito.when(db.collection("users/x/cards").get().
                addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).
                thenAnswer((obj) ->
                {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(qsResult);
                    return null;
                });

        DocumentReference documentReferenceMock = Mockito.mock(DocumentReference.class);
        Mockito.when(db.document("users/x")).thenReturn(documentReferenceMock);

        Task<DocumentSnapshot> userResult = Mockito.mock(Task.class);
        Mockito.when(userResult.isSuccessful()).thenReturn(true);
        Mockito.when(userResult.getResult()).thenReturn(Mockito.mock(DocumentSnapshot.class));
        // tades made is 5
        Mockito.when(userResult.getResult().get("tradesmade")).thenReturn((long)5);

        Mockito.when(db.document("users/x").get()).thenReturn(userResult);
        Mockito.when(db.document("users/x").get().
                        addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).
                thenAnswer((obj) ->
                {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(userResult);
                    return null;
                });

        FirebaseUser userMock = Mockito.mock(FirebaseUser.class);
        // email is unit@test.com
        Mockito.when(userMock.getEmail()).thenReturn("unit@test.com");
        Mockito.when(auth.getCurrentUser()).thenReturn(userMock);

        data.requestProfile();
        HashMap<String, Object> expected = new HashMap<>();
        expected.put("email", "unit@test.com");
        expected.put("cardamount", 2);
        expected.put("tradesmade", 5);

        Mockito.verify(repo).receiverProfile(expected);
    }
}
