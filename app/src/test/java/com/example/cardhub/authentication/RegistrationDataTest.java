package com.example.cardhub.authentication;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
}