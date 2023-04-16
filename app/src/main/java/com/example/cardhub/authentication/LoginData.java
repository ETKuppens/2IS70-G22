package com.example.cardhub.authentication;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Data Design Pattern implementation for the signing in of users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class LoginData {
    // Variables
    private LoginReceiver receiver; // Receiver to be used after task execution
    private FirebaseAuth mAuth; // Firebase authentication instance
    private FirebaseFirestore db; // Firebase firestore instance
    private FirebaseUser user; // Firebase user instance

    /**
     * Constructs a new LoginData instance using the given {@code receiver}
     * instance.
     *
     * @param receiver given receiver instance
     */
    public LoginData(LoginReceiver receiver) {
        this.receiver = receiver;
        this.mAuth = FirebaseAuth.getInstance();

    }

    /**
     * Signs in the client using the given {@code email} and {@code password}.
     *
     * @param email email used to sign in
     * @param password password used to sign in
     */
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            // Move to next screen
                            receiver.signInSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            receiver.signInFail();
                        }
                    }
                });
    }

    /**
     * Retrieves the user account that has been logged in.
     */
    public void getCurrentUser() {
        receiver.receiveCurrentUser(mAuth.getCurrentUser());
    }

    /**
     * Retrieves the role of the {@code user}.
     *
     * @param user current user account that has been logged in
     */
    public void getUserRole(FirebaseUser user) {
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        String uid = user.getUid();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Read data
                        String role = (String) document.get("role");
                        receiver.userRoleCallback(role);
                    } else {
                        Log.d(TAG, "No account!");
                        receiver.userRoleCallback("");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
