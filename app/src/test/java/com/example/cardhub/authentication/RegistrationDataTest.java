package com.example.cardhub.authentication;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

/**
 * Tests RegistrationData.
 */
public class RegistrationDataTest{
    // Variables
    @Mock
    private FirebaseAuth mAuth;
    @Mock
    private FirebaseFirestore db;
    @Mock
    private RegistrationReceiver receiver;
    private final RegistrationData data;
    @Captor
    private ArgumentCaptor<OnCompleteListener> onCompleteListenerArgumentCaptor;
    @Captor
    private ArgumentCaptor<OnSuccessListener> onSuccessListenerArgumentCaptor;
    @Captor
    private ArgumentCaptor<OnFailureListener> onFailureListenerArgumentCaptor;

    public RegistrationDataTest() {
        MockitoAnnotations.openMocks(this);
        this.data = new RegistrationData(receiver, mAuth, db);
    }

    /**
     * Test that {@code RegistrationData} propagates a NullPointerException when {@code receiver == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationDataNull0() {
        new RegistrationData(null);
    }

    /**
     * Test that {@code RegistrationData} propagates a NullPointerException when {@code receiver == null && mAuth == null && db == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationDataNull1() {
        new RegistrationData(null, null, null);
    }

    /**
     * Test that {@code RegistrationData} propagates a NullPointerException when {@code receiver != null && mAuth == null && db == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationDataNull2() {
        new RegistrationData(receiver, null, null);
    }

    /**
     * Test that {@code RegistrationData} propagates a NullPointerException when {@code receiver == null && mAuth != null && db == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationDataNull3() {
        new RegistrationData(null, mAuth, null);
    }

    /**
     * Test that {@code RegistrationData} propagates a NullPointerException when {@code receiver == null && mAuth == null && db != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationDataNull4() {
        new RegistrationData(null, null, db);
    }

    /**
     * Test that {@code RegistrationData} propagates a NullPointerException when {@code receiver != null && mAuth != null && db == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationDataNull5() {
        new RegistrationData(receiver, mAuth, null);
    }

    /**
     * Test that {@code RegistrationData} propagates a NullPointerException when {@code receiver == null && mAuth != null && db != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationDataNull6() {
        new RegistrationData(null, mAuth, db);
    }

    /**
     * Test that {@code RegistrationData} propagates a NullPointerException when {@code receiver != null && mAuth == null && db != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegistrationDataNull7() {
        new RegistrationData(receiver, null, db);
    }

    /**
     * Test that {@code register} calls {@code registerSuccess} on {@code receiver} when given proper parameters.
     */
    @Test
    public void testRegisterSuccess() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "testtest";
        final String role = "Card Collector";
        final Task createUserWithEmailAndPasswordTask = Mockito.mock(Task.class);
        final FirebaseUser sendRoleUser = Mockito.mock(FirebaseUser.class);
        final CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
        final DocumentReference documentReferenceMock = Mockito.mock(DocumentReference.class);
        final Task setTask = Mockito.mock(Task.class);
        final String docPath = "uid";
        final HashMap<String, Object> userEntry = new HashMap<>();

        // HashMap Content
        userEntry.put("role", role);
        userEntry.put("tradesmade", 0);

        // Adjust mock
        // Register mock
        Mockito.when(mAuth.createUserWithEmailAndPassword(emailAddress, password))
                .thenReturn(createUserWithEmailAndPasswordTask);
        Mockito.when(createUserWithEmailAndPasswordTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(createUserWithEmailAndPasswordTask);
                    return null;
                }
        );
        Mockito.when(createUserWithEmailAndPasswordTask.isSuccessful()).thenReturn(true);
        Mockito.when(mAuth.getCurrentUser()).thenReturn(sendRoleUser);

        // UploadUserEntry
        Mockito.when(db.collection("users")).thenReturn(collectionReferenceMock);
        Mockito.when(collectionReferenceMock.document(docPath)).thenReturn(documentReferenceMock);
        Mockito.when(sendRoleUser.getUid()).thenReturn(docPath);
        Mockito.when(documentReferenceMock.set(userEntry)).thenReturn(setTask);
        Mockito.when(setTask.addOnSuccessListener(onSuccessListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onSuccessListenerArgumentCaptor.getValue().onSuccess(null);
                    return setTask;
                }
        );

        // Act
        data.register(emailAddress, password, role);

        // Verify
        Mockito.verify(receiver).registrationSuccess(role);
    }

    /**
     * Test that {@code register} calls {@code registrationDatabaseFail} when {@code set} fails.
     */
    @Test
    public void testRegisterFailure0() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "testtest";
        final String role = "Card Collector";
        final Task createUserWithEmailAndPasswordTask = Mockito.mock(Task.class);
        final FirebaseUser sendRoleUser = Mockito.mock(FirebaseUser.class);
        final CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
        final DocumentReference documentReferenceMock = Mockito.mock(DocumentReference.class);
        final Task setTask = Mockito.mock(Task.class);
        final String docPath = "uid";
        final HashMap<String, Object> userEntry = new HashMap<>();
        final Exception e = Mockito.mock(Exception.class);

        // HashMap Content
        userEntry.put("role", role);
        userEntry.put("tradesmade", 0);

        // Adjust mock
        // Register mock
        Mockito.when(mAuth.createUserWithEmailAndPassword(emailAddress, password))
                .thenReturn(createUserWithEmailAndPasswordTask);
        Mockito.when(createUserWithEmailAndPasswordTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(createUserWithEmailAndPasswordTask);
                    return null;
                }
        );
        Mockito.when(createUserWithEmailAndPasswordTask.isSuccessful()).thenReturn(true);
        Mockito.when(mAuth.getCurrentUser()).thenReturn(sendRoleUser);

        // UploadUserEntry
        Mockito.when(db.collection("users")).thenReturn(collectionReferenceMock);
        Mockito.when(collectionReferenceMock.document(docPath)).thenReturn(documentReferenceMock);
        Mockito.when(sendRoleUser.getUid()).thenReturn(docPath);
        Mockito.when(documentReferenceMock.set(userEntry)).thenReturn(setTask);
        Mockito.when(setTask.addOnSuccessListener(onSuccessListenerArgumentCaptor.capture())).thenReturn(setTask);
        Mockito.when(setTask.addOnFailureListener(onFailureListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onFailureListenerArgumentCaptor.getValue().onFailure(e);
                    return null;
                }
        );

        // Act
        data.register(emailAddress, password, role);

        // Verify
        Mockito.verify(receiver).registrationDatabaseFail();
    }

    /**
     * Test that {@code register} propagates a NullException when {@code user == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testUploadUserEntryNullException() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "testtest";
        final String role = "Card Collector";
        final Task createUserWithEmailAndPasswordTask = Mockito.mock(Task.class);
        final HashMap<String, Object> userEntry = new HashMap<>();

        // HashMap Content
        userEntry.put("role", role);
        userEntry.put("tradesmade", 0);

        // Adjust mock
        // Register mock
        Mockito.when(mAuth.createUserWithEmailAndPassword(emailAddress, password))
                .thenReturn(createUserWithEmailAndPasswordTask);
        Mockito.when(createUserWithEmailAndPasswordTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(createUserWithEmailAndPasswordTask);
                    return null;
                }
        );
        Mockito.when(createUserWithEmailAndPasswordTask.isSuccessful()).thenReturn(true);
        Mockito.when(mAuth.getCurrentUser()).thenReturn(null);

        // Act
        data.register(emailAddress, password, role);
    }

    /**
     * Test that {@code register} calls {@code registrationDatabaseFail} on {@code receiver} when given proper parameters.
     */
    @Test
    public void testRegisterFailure1() {
        // Variables
        final String emailAddress = "a.oostenbrug@student.tue.nl";
        final String password = "testtest";
        final String role = "Card Collector";
        final Task createUserWithEmailAndPasswordTask = Mockito.mock(Task.class);
        final HashMap<String, Object> userEntry = new HashMap<>();

        // HashMap Content
        userEntry.put("role", role);
        userEntry.put("tradesmade", 0);

        // Adjust mock
        // Register mock
        Mockito.when(mAuth.createUserWithEmailAndPassword(emailAddress, password))
                .thenReturn(createUserWithEmailAndPasswordTask);
        Mockito.when(createUserWithEmailAndPasswordTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(createUserWithEmailAndPasswordTask);
                    return null;
                }
        );
        Mockito.when(createUserWithEmailAndPasswordTask.isSuccessful()).thenReturn(false);

        // Act
        data.register(emailAddress, password, role);

        // Verify
        Mockito.verify(receiver).registrationDatabaseFail();
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress == null && password != null && role != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException0() {
        data.register(null, "testtest", "Card Collector");
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress != null && password == null && role != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException1() {
        data.register("a.oostenbrug@student.tue.nl", null, "Card Collector");
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress != null && password != null && role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException2() {
        data.register("a.oostenbrug@student.tue.nl", "testtest", null);
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress != null && password == null && role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException3() {
        data.register("a.oostenbrug@student.tue.nl", null, null);
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress == null && password != null && role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException4() {
        data.register(null, "testtest", null);
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress == null && password == null && role != null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException5() {
        data.register(null, null, "Card Collector");
    }

    /**
     * Test that {@code register} propagates a NullException when {@code emailAddress == null && password == null && role == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterNullException6() {
        data.register(null, null, null);
    }
}