package com.example.cardhub.authentication;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordDataTest {
    @Mock
    private FirebaseAuth mAuth;
    @Mock
    private ForgotPasswordReceiver receiver;
    private final ForgotPasswordData data;

    @Captor
    ArgumentCaptor<OnCompleteListener> onCompleteListenerArgumentCaptor;

    public ForgotPasswordDataTest() {
        MockitoAnnotations.openMocks(this);
        this.data = new ForgotPasswordData(receiver, mAuth);
    }

    /**
     * Test that {@code sendForgotPasswordEmail} calls {@code sendForgotPasswordSuccess} when it succeeds.
     */
    @Test
    public void testSendForgotPasswordEmailSuccess() {
        // Variables
        String emailAddress = "rebuilded2.0@gmail.com";

        // Adjust mock
        Task sendPasswordResetEmailTask = Mockito.mock(Task.class);
        Mockito.when(sendPasswordResetEmailTask.isSuccessful()).thenReturn(true);

        Mockito.when(mAuth.sendPasswordResetEmail(emailAddress))
                .thenReturn(sendPasswordResetEmailTask);

        Mockito.when(sendPasswordResetEmailTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(sendPasswordResetEmailTask);
                    return null;
                }
        );

        // Act
        data.sendForgotPasswordEmail(emailAddress);

        // Verify
        Mockito.verify(receiver).sendForgotPasswordEmailSuccess();
    }

    /**
     * Test that {@code sendForgotPasswordEmail} calls {@code sendForgotPasswordDatabaseFailure} when it fails.
     */
    @Test
    public void testSendForgotPasswordEmailFailure() {
        // Variables
        String emailAddress = "rebuilded2.0@gmail.com";

        // Adjust mock
        Task sendPasswordResetEmailTask = Mockito.mock(Task.class);

        Mockito.when(sendPasswordResetEmailTask.isSuccessful()).thenReturn(false);
        Mockito.when(mAuth.sendPasswordResetEmail(emailAddress))
                .thenReturn(sendPasswordResetEmailTask);
        Mockito.when(sendPasswordResetEmailTask.addOnCompleteListener(onCompleteListenerArgumentCaptor.capture())).thenAnswer(
                (obj) -> {
                    onCompleteListenerArgumentCaptor.getValue().onComplete(sendPasswordResetEmailTask);
                    return null;
                }
        );

        // Act
        data.sendForgotPasswordEmail(emailAddress);

        // Verify
        Mockito.verify(receiver).sendForgotPasswordEmailDatabaseFailure();
    }

    /**
     * Test that {@code ForgotPasswordData} propagates a NullPointerException when {@code receiver == null || mAuth == null}.
     */
    @Test(expected = NullPointerException.class)
    public void testForgotPasswordDataNull() {
        new ForgotPasswordData(null);
        new ForgotPasswordData(null, null);
        new ForgotPasswordData(receiver, null);
        new ForgotPasswordData(null, mAuth);
    }
}