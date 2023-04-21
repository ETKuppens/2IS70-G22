package com.example.cardhub.authentication;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Tests LoginData.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 21-04-2023
 */
public class LoginDataTest {
    @Mock
    private LoginReceiver receiver;
    @Mock
    private FirebaseAuth mAuth;
    @Mock
    private FirebaseFirestore db;
    private final LoginData data;
    @Captor
    private ArgumentCaptor<OnCompleteListener> onCompleteListenerArgumentCaptor;

    public LoginDataTest() {
        MockitoAnnotations.openMocks(this);
        this.data = new LoginData(receiver, mAuth, db);
    }

    /**
     * Test that {@code LoginData} propagates a NullPointerException when {@code receiver == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginDataNull0() {
        new LoginData(null);
    }

    /**
     * Test that {@code LoginData} propagates a NullPointerException when {@code receiver == null && mAuth != null && db != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginDataNull1() {
        new LoginData(null, mAuth, db);
    }

    /**
     * Test that {@code LoginData} propagates a NullPointerException when {@code receiver != null && mAuth == null && db != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginDataNull2() {
        new LoginData(receiver, null, db);
    }

    /**
     * Test that {@code LoginData} propagates a NullPointerException when {@code receiver != null && mAuth != null && db == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginDataNull3() {
        new LoginData(receiver, mAuth, null);
    }

    /**
     * Test that {@code LoginData} propagates a NullPointerException when {@code receiver != null && mAuth == null && db == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginDataNull4() {
        new LoginData(receiver, null, null);
    }

    /**
     * Test that {@code LoginData} propagates a NullPointerException when {@code receiver == null && mAuth != null && db == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginDataNull5() {
        new LoginData(null, mAuth, null);
    }

    /**
     * Test that {@code LoginData} propagates a NullPointerException when {@code receiver == null && mAuth == null && db != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginDataNull6() {
        new LoginData(null, null, db);
    }

    /**
     * Test that {@code LoginData} propagates a NullPointerException when {@code receiver == null && mAuth == null && db == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testLoginDataNull7() {
        new LoginData(null, null, null);
    }

    /**
     * Test that {@code signIn} propagates a NullPointerException when {@code emailAddress == null && password == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testSignInNullException0() {
        data.signIn(null, null);
    }

    /**
     * Test that {@code signIn} propagates a NullPointerException when {@code emailAddress != null && password == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testSignInNullException1() {
        data.signIn("a.oostenbrug@student.tue.nl", null);
    }

    /**
     * Test that {@code signIn} propagates a NullPointerException when {@code emailAddress == null && password != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testSignInNullException2() {
        data.signIn(null, "testtest");
    }

    /**
     * Test that {@code signIn} calls {@code signInSuccess} on {@code receiver} when given proper parameter.
     */
    @Test
    public void testSignInSuccess() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "testtest";
        final String role = "Card Collector";
        final Task signInWithEmailAndPasswordTask = Mockito.mock(Task.class);
        final FirebaseUser sendRoleUser = Mockito.mock(FirebaseUser.class);
        final CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
        final DocumentReference documentReferenceMock = Mockito.mock(DocumentReference.class);
        final Task getTask = Mockito.mock(Task.class);
        final DocumentSnapshot documentSnapshotMock = Mockito.mock(DocumentSnapshot.class);
        final String docPath = "uid";

        // Adjust mock
        // SignIn mocking
        Mockito.when(mAuth.signInWithEmailAndPassword(emailAddress, password))
                .thenReturn(signInWithEmailAndPasswordTask);
        Mockito.when(signInWithEmailAndPasswordTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(signInWithEmailAndPasswordTask);
                    return null;
                }
        );
        Mockito.when(signInWithEmailAndPasswordTask.isSuccessful()).thenReturn(true);
        Mockito.when(mAuth.getCurrentUser()).thenReturn(sendRoleUser);
        Mockito.when(sendRoleUser.getUid()).thenReturn(docPath);

        // SendRole mocking
        Mockito.when(db.collection("users")).thenReturn(collectionReferenceMock);
        Mockito.when(collectionReferenceMock.document(docPath)).thenReturn(documentReferenceMock);
        Mockito.when(documentReferenceMock.get()).thenReturn(getTask);
        Mockito.when(getTask.isSuccessful()).thenReturn(true);
        Mockito.when(getTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(getTask);
                    return null;
                }
        );
        Mockito.when(getTask.getResult()).thenReturn(documentSnapshotMock);
        Mockito.when(documentSnapshotMock.exists()).thenReturn(true);
        Mockito.when(documentSnapshotMock.get("role")).thenReturn(role);

        // Act
        data.signIn(emailAddress, password);

        // Verify
        Mockito.verify(receiver).signInSuccess(role);
    }

    /**
     * Test that {@code signIn} calls {@code signInDatabaseFail} on {@code receiver} when the user document can not be found.
     */
    @Test
    public void testSignInDatabaseFail0() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "testtest";
        final String role = "Card Collector";
        final Task signInWithEmailAndPasswordTask = Mockito.mock(Task.class);
        final FirebaseUser sendRoleUser = Mockito.mock(FirebaseUser.class);
        final CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
        final DocumentReference documentReferenceMock = Mockito.mock(DocumentReference.class);
        final Task getTask = Mockito.mock(Task.class);
        final DocumentSnapshot documentSnapshotMock = Mockito.mock(DocumentSnapshot.class);
        final String docPath = "uid";

        // Adjust mock
        // SignIn mocking
        Mockito.when(mAuth.signInWithEmailAndPassword(emailAddress, password))
                .thenReturn(signInWithEmailAndPasswordTask);
        Mockito.when(signInWithEmailAndPasswordTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(signInWithEmailAndPasswordTask);
                    return null;
                }
        );
        Mockito.when(signInWithEmailAndPasswordTask.isSuccessful()).thenReturn(true);
        Mockito.when(mAuth.getCurrentUser()).thenReturn(sendRoleUser);
        Mockito.when(sendRoleUser.getUid()).thenReturn(docPath);

        // SendRole mocking
        Mockito.when(db.collection("users")).thenReturn(collectionReferenceMock);
        Mockito.when(collectionReferenceMock.document(docPath)).thenReturn(documentReferenceMock);
        Mockito.when(documentReferenceMock.get()).thenReturn(getTask);
        Mockito.when(getTask.isSuccessful()).thenReturn(true);
        Mockito.when(getTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(getTask);
                    return null;
                }
        );
        Mockito.when(getTask.getResult()).thenReturn(documentSnapshotMock);
        Mockito.when(documentSnapshotMock.exists()).thenReturn(false);

        // Act
        data.signIn(emailAddress, password);

        // Verify
        Mockito.verify(receiver).signInDatabaseFail();
    }

    /**
     * Test that {@code signIn} calls {@code signInDatabaseFail} on {@code receiver} when the database read attempts fails.
     */
    @Test
    public void testSignInDatabaseFail1() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "testtest";
        final String role = "Card Collector";
        final Task signInWithEmailAndPasswordTask = Mockito.mock(Task.class);
        final FirebaseUser sendRoleUser = Mockito.mock(FirebaseUser.class);
        final CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
        final DocumentReference documentReferenceMock = Mockito.mock(DocumentReference.class);
        final Task getTask = Mockito.mock(Task.class);
        final String docPath = "uid";

        // Adjust mock
        // SignIn mocking
        Mockito.when(mAuth.signInWithEmailAndPassword(emailAddress, password))
                .thenReturn(signInWithEmailAndPasswordTask);
        Mockito.when(signInWithEmailAndPasswordTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(signInWithEmailAndPasswordTask);
                    return null;
                }
        );
        Mockito.when(signInWithEmailAndPasswordTask.isSuccessful()).thenReturn(true);
        Mockito.when(mAuth.getCurrentUser()).thenReturn(sendRoleUser);
        Mockito.when(sendRoleUser.getUid()).thenReturn(docPath);

        // SendRole mocking
        Mockito.when(db.collection("users")).thenReturn(collectionReferenceMock);
        Mockito.when(collectionReferenceMock.document(docPath)).thenReturn(documentReferenceMock);
        Mockito.when(documentReferenceMock.get()).thenReturn(getTask);
        Mockito.when(getTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(getTask);
                    return null;
                }
        );
        Mockito.when(getTask.isSuccessful()).thenReturn(false);

        // Act
        data.signIn(emailAddress, password);

        // Verify
        Mockito.verify(receiver).signInDatabaseFail();
    }

    /**
     * Test that {@code signIn} propagates a NullException when {@code mAuth.getCurrentUser()} returns {@code null}.
     */
    @Test(expected = NullPointerException.class)
    public void testSignInDatabaseFail2() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "testtest";
        final Task signInWithEmailAndPasswordTask = Mockito.mock(Task.class);
        final FirebaseUser sendRoleUser = Mockito.mock(FirebaseUser.class);
        final String docPath = "uid";

        // Adjust mock
        // SignIn mocking
        Mockito.when(mAuth.signInWithEmailAndPassword(emailAddress, password))
                .thenReturn(signInWithEmailAndPasswordTask);
        Mockito.when(signInWithEmailAndPasswordTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(signInWithEmailAndPasswordTask);
                    return null;
                }
        );
        Mockito.when(signInWithEmailAndPasswordTask.isSuccessful()).thenReturn(true);
        Mockito.when(sendRoleUser.getUid()).thenReturn(docPath);
        Mockito.when(mAuth.getCurrentUser()).thenReturn(null);

        // Act
        data.signIn(emailAddress, password);

        // Verify
        Mockito.verify(receiver).signInDatabaseFail();
    }


    /**
     * Test that {@code signIn} calls {@code signInDatabaseFail} on {@code receiver} when the sign-in attempt fails.
     */
    @Test
    public void testSignInDatabaseFail3() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "testtest";
        final Task signInWithEmailAndPasswordTask = Mockito.mock(Task.class);

        // Adjust mock
        // SignIn mocking
        Mockito.when(mAuth.signInWithEmailAndPassword(emailAddress, password))
                .thenReturn(signInWithEmailAndPasswordTask);
        Mockito.when(signInWithEmailAndPasswordTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(signInWithEmailAndPasswordTask);
                    return null;
                }
        );
        Mockito.when(signInWithEmailAndPasswordTask.isSuccessful()).thenReturn(false);

        // Act
        data.signIn(emailAddress, password);

        // Verify
        Mockito.verify(receiver).signInDatabaseFail();
    }

    /**
     * Test that {@code signInSignedInUsers} does not crash when {@code currentUser == null}.
     */
    @Test
    public void testSignInSignedInUsersNull(){
        // Variables
        // Adjust mock
        Mockito.when(mAuth.getCurrentUser()).thenReturn(null);

        // Act
        data.signInSignedInUsers();
    }

    /**
     * Test that {@code signInSignedInUsers} calls {@code signInSuccess} when {@code currentUser != null}. the database read attempt was successful, and the user document was found.
     */
    @Test
    public void testSignInSignedInUsersSuccess(){
        // Variables
        final FirebaseUser currentUser = Mockito.mock(FirebaseUser.class);
        final CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
        final DocumentReference documentReferenceMock = Mockito.mock(DocumentReference.class);
        final Task getTask = Mockito.mock(Task.class);
        final String docPath = "uid";
        final String role = "Card Collector";
        final DocumentSnapshot documentSnapshotMock = Mockito.mock(DocumentSnapshot.class);

        // Adjust mock
        // Mock signInSignedInUsers
        Mockito.when(mAuth.getCurrentUser()).thenReturn(currentUser);

        // SendRole mocking
        Mockito.when(db.collection("users")).thenReturn(collectionReferenceMock);
        Mockito.when(currentUser.getUid()).thenReturn(docPath);
        Mockito.when(collectionReferenceMock.document(docPath)).thenReturn(documentReferenceMock);
        Mockito.when(documentReferenceMock.get()).thenReturn(getTask);
        Mockito.when(getTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(getTask);
                    return null;
                }
        );
        Mockito.when(getTask.isSuccessful()).thenReturn(true);
        Mockito.when(getTask.getResult()).thenReturn(documentSnapshotMock);
        Mockito.when(documentSnapshotMock.exists()).thenReturn(true);
        Mockito.when(documentSnapshotMock.get("role")).thenReturn(role);

        // Act
        data.signInSignedInUsers();

        // Verify
        Mockito.verify(receiver).signInSuccess(role);
    }

    /**
     * Test that {@code signInSignedInUsers} calls {@code signInSuccess} when {@code currentUser != null}. the database read attempt was successful, and the user document was found.
     */
    @Test
    public void testSignInSignedInUsersDatabaseFailure0(){
        // Variables
        final FirebaseUser currentUser = Mockito.mock(FirebaseUser.class);
        final CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
        final DocumentReference documentReferenceMock = Mockito.mock(DocumentReference.class);
        final Task getTask = Mockito.mock(Task.class);
        final String docPath = "uid";
        final String role = "Card Collector";
        final DocumentSnapshot documentSnapshotMock = Mockito.mock(DocumentSnapshot.class);

        // Adjust mock
        // Mock signInSignedInUsers
        Mockito.when(mAuth.getCurrentUser()).thenReturn(currentUser);

        // SendRole mocking
        Mockito.when(db.collection("users")).thenReturn(collectionReferenceMock);
        Mockito.when(currentUser.getUid()).thenReturn(docPath);
        Mockito.when(collectionReferenceMock.document(docPath)).thenReturn(documentReferenceMock);
        Mockito.when(documentReferenceMock.get()).thenReturn(getTask);
        Mockito.when(getTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(getTask);
                    return null;
                }
        );
        Mockito.when(getTask.isSuccessful()).thenReturn(true);
        Mockito.when(getTask.getResult()).thenReturn(documentSnapshotMock);
        Mockito.when(documentSnapshotMock.exists()).thenReturn(false);

        // Act
        data.signInSignedInUsers();

        // Verify
        Mockito.verify(receiver).signInDatabaseFail();
    }

    /**
     * Test that {@code signInSignedInUsers} calls {@code signInSuccess} when {@code currentUser != null}, but the database read attempt was unsuccessful.
     */
    @Test
    public void testSignInSignedInUsersDatabaseFailure1(){
        // Variables
        final FirebaseUser currentUser = Mockito.mock(FirebaseUser.class);
        final CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
        final DocumentReference documentReferenceMock = Mockito.mock(DocumentReference.class);
        final Task getTask = Mockito.mock(Task.class);
        final String docPath = "uid";
        final DocumentSnapshot documentSnapshotMock = Mockito.mock(DocumentSnapshot.class);

        // Adjust mock
        // Mock signInSignedInUsers
        Mockito.when(mAuth.getCurrentUser()).thenReturn(currentUser);

        // SendRole mocking
        Mockito.when(db.collection("users")).thenReturn(collectionReferenceMock);
        Mockito.when(currentUser.getUid()).thenReturn(docPath);
        Mockito.when(collectionReferenceMock.document(docPath)).thenReturn(documentReferenceMock);
        Mockito.when(documentReferenceMock.get()).thenReturn(getTask);
        Mockito.when(getTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(getTask);
                    return null;
                }
        );
        Mockito.when(getTask.isSuccessful()).thenReturn(false);
        Mockito.when(getTask.getResult()).thenReturn(documentSnapshotMock);

        // Act
        data.signInSignedInUsers();

        // Verify
        Mockito.verify(receiver).signInDatabaseFail();
    }
}