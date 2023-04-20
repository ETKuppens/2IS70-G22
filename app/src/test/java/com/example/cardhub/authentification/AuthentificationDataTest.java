package com.example.cardhub.authentification;

import com.example.cardhub.card_creation.CardCreationData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import junit.framework.TestCase;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AuthentificationDataTest extends TestCase {


    @Mock
    FirebaseAuth auth;
    @Mock
    FirebaseFirestore db;
    @Mock
    AuthentificationReciever receiver;

    AuthentificationData data;

    @Captor
    ArgumentCaptor<OnCompleteListener> onCompleteListenerArgumentCaptor;


    public AuthentificationDataTest() {
        MockitoAnnotations.openMocks(this);
        this.data = new AuthentificationData(receiver, auth, db);
    }

    public void testSignIn() {
        // Adjust mock
        Task authResultTaskMock = Mockito.mock(Task.class);
        Mockito.when(authResultTaskMock.isSuccessful()).thenReturn(true);

        Mockito.when(auth.signInWithEmailAndPassword("email", "password"))
                .thenReturn(authResultTaskMock);

        Mockito.when(authResultTaskMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(authResultTaskMock);
                    return null;
                }
        );

        // Act
        data.signIn("email", "password");

        // Validate
        Mockito.verify(receiver).signInSuccess(Mockito.any());
    }

    public void testSignInFail() {
        // Adjust mock
        Task authResultTaskMock = Mockito.mock(Task.class);
        Mockito.when(authResultTaskMock.isSuccessful()).thenReturn(false);

        Mockito.when(auth.signInWithEmailAndPassword("email", "password"))
                .thenReturn(authResultTaskMock);

        Mockito.when(authResultTaskMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(authResultTaskMock);
                    return null;
                }
        );

        // Act
        data.signIn("email", "password");

        // Validate
        Mockito.verify(receiver).signInFail();
    }

    public void testGetCurrentUser() {
        // Adjust mock
        FirebaseUser userMock = Mockito.mock(FirebaseUser.class);
        Mockito.when(auth.getCurrentUser()).thenReturn(userMock);

        // Act
        data.getCurrentUser();

        // Verify
        Mockito.verify(receiver).recieveCurrentUser(userMock);
    }

    public void testGetUserRole() {
        // Adjust mock
        FirebaseUser userMock = Mockito.mock(FirebaseUser.class);
        Mockito.when(userMock.getUid()).thenReturn("x");

        Mockito.when(auth.getCurrentUser()).thenReturn(userMock);

        CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("users")).thenReturn(collectionReferenceMock);

        DocumentReference documentReferenceMock = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReferenceMock.document("x")).thenReturn(documentReferenceMock);

        Task taskMock = Mockito.mock(Task.class);
        Mockito.when(taskMock.isSuccessful()).thenReturn(true);

        Mockito.when(documentReferenceMock.get()).thenReturn(taskMock);

        Mockito.when(taskMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(taskMock);
                    return null;
                }
        );

        DocumentSnapshot documentSnapshotMock = Mockito.mock(DocumentSnapshot.class);
        Mockito.when(taskMock.getResult()).thenReturn(documentSnapshotMock);

        Mockito.when(documentSnapshotMock.exists()).thenReturn(true);

        Mockito.when(documentSnapshotMock.get("role")).thenReturn("test role");
        // Act
        data.getUserRole(userMock);

        // Verify
        Mockito.verify(receiver).userRoleCallback("test role");
    }

    public void testGetUserRoleNoAccount() {
        // Adjust mock
        FirebaseUser userMock = Mockito.mock(FirebaseUser.class);
        Mockito.when(userMock.getUid()).thenReturn("x");

        Mockito.when(auth.getCurrentUser()).thenReturn(userMock);

        CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
        Mockito.when(db.collection("users")).thenReturn(collectionReferenceMock);

        DocumentReference documentReferenceMock = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReferenceMock.document("x")).thenReturn(documentReferenceMock);

        Task taskMock = Mockito.mock(Task.class);
        Mockito.when(taskMock.isSuccessful()).thenReturn(true);

        Mockito.when(documentReferenceMock.get()).thenReturn(taskMock);

        Mockito.when(taskMock.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(taskMock);
                    return null;
                }
        );

        DocumentSnapshot documentSnapshotMock = Mockito.mock(DocumentSnapshot.class);
        Mockito.when(taskMock.getResult()).thenReturn(documentSnapshotMock);

        Mockito.when(documentSnapshotMock.exists()).thenReturn(false);

        // Act
        data.getUserRole(userMock);

        // Verify
        Mockito.verify(receiver).userRoleCallback("");
    }

}