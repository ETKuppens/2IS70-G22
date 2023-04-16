package com.example.cardhub.authentication;

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
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * Data Design Pattern implementation for the signing in of users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class LoginData {
    // Constants
    public static final String TAG = "LoginData";

    // Variables
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Firebase authentication instance
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();; // Firebase firestore instance
    private FirebaseUser user; // Firebase user instance
    private LoginReceiver receiver; // Receiver to be used after task execution

    /**
     * Constructs a new LoginData instance using the given {@code receiver}
     * instance.
     *
     * @param receiver given receiver instance
     *
     * @throws NullPointerException if {@code receiver == null}
     */
    public LoginData(LoginReceiver receiver) throws NullPointerException {
        if (receiver == null) {
            throw new NullPointerException(
                    "LoginData.LoginData.pre violated: receiver == null"
            );
        }

        this.receiver = receiver;
    }

    /**
     * Signs in the client using the given {@code email} and {@code password}.
     *
     * @pre {@code email != null && password != null}
     *
     * @param email email used to sign in
     * @param password password used to sign in
     *
     * @throws NullPointerException if {@code email == null || password == null}
     */
    public void signIn(String email, String password) throws NullPointerException {
        if (email == null) {
            throw new NullPointerException("LoginData.signIn.pre violated: email == null");
        }

        if (password == null) {
            throw new NullPointerException("LoginData.signIn.pre violated: password == null");
        }

        // Attempt to sign in using the given credential
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // Authentication request has been completed
                    if (task.isSuccessful()) {  // Sign in attempt was
                                                // successful
                        // Log success
                        Log.d(TAG, "signInWithEmail:success");

                        user = mAuth.getCurrentUser(); // Update user

                        receiver.signInSuccess(user); // Update UI
                    } else { // Sign in attempt was unsuccessful
                        Log.w(TAG, "signInWithEmail:failure",
                                task.getException()); // Log failure
                        receiver.signInFail(); // Display error message
                    }
                }
            });
    }

    /**
     * Retrieves the user account that has been logged in.
     *
     * @pre {@code user != null}
     *
     * @throws NullPointerException if {@code user == null}
     */
    public void getCurrentUser() throws NullPointerException {
        if (user == null) {
            throw new NullPointerException("LoginData.getCurrentUser.pre violated: user == null");
        }

        receiver.receiveCurrentUser(user); // State callback
    }

    /**
     * Retrieves the role of the {@code user}.
     *
     * @param currentUser current user account that has been logged in
     */
    public void getUserRole(FirebaseUser currentUser) {
        if (currentUser == null) {
            throw new NullPointerException("LoginData.getUserRole.pre violated: currentUser == null");
        }

        // Local variables
        String uid = currentUser.getUid();
        DocumentReference docRef = db.collection("users").document(uid);

        // Attempt to get user info
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Read request has been completed
                if (task.isSuccessful()) { // Read attempt was successful
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) { // User document was found
                        Log.d(TAG, "getUserRole:success"); // Log success

                        // Read role
                        String role = (String) document.get("role");

                        receiver.userRoleCallback(role); // Update UI
                    } else { // User document could not be found or empty
                        // Initialize exception
                        IllegalArgumentException exception =
                                new IllegalArgumentException(
                                        "No such user in the database."
                                );

                        // Log failure
                        Log.e(TAG, "getUserRole:failure-", exception);

                        throw exception;
                    }
                } else { // Read attempt failed
                    // Log failure
                    Log.w(TAG, "getUserRole:failure+", task.getException());
                }
            }
        });
    }
}
