package com.example.cardhub.authentication;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Data Design Pattern implementation for signing in users.
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
    private final LoginReceiver receiver;

    /**
     * Constructs a new LoginData instance using the given {@code receiver}
     * instance.
     *
     * @param receiver given receiver instance
     * @pre {@code receiver != null}
     * @throws NullPointerException if {@code receiver == null}
     * @post instance is initialized
     */
    public LoginData(LoginReceiver receiver) throws NullPointerException {
        // Precondition testing
        // Receiver precondition test
        if (receiver == null) {
            throw new NullPointerException(
                    "LoginData.LoginData.pre violated: receiver == null"
            );
        }

        this.receiver = receiver;
    }

    /**
     * Signs in the client using the given {@code emailAddress} and {@code password}.
     *
     * @pre {@code emailAddress != null && password != null}
     * @param emailAddress emailAddress used to sign in
     * @param password password used to sign in
     * @throws NullPointerException if {@code emailAddress == null || password == null}
     * @post an account has been signed-in to
     */
    public void signIn(String emailAddress, String password) throws NullPointerException {
        // Precondition testing
        // Email precondition test
        if (emailAddress == null) {
            throw new NullPointerException("LoginData.signIn.pre violated: emailAddress == null");
        }

        // Password precondition test
        if (password == null) {
            throw new NullPointerException("LoginData.signIn.pre violated: password == null");
        }

        // Variables
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Attempt to sign in
        mAuth.signInWithEmailAndPassword(emailAddress, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {  // Signed-in successfully
                    Log.d(TAG, "signInWithEmail:success"); // Log success

                    // Variables
                    final FirebaseUser user = mAuth.getCurrentUser();

                    sendRole(user); // Get user role
                } else { // Sign in attempt was unsuccessful
                    Log.w(TAG, "signInWithEmail:failure", task.getException()); // Log failure
                    receiver.signInFail(); // Propagate failure signal
                }
            });
    }

    /**
     * Propagates the role of the given {@code user}.
     *
     * @param user user which has been signed-in
     * @throws NullPointerException if {@code user == null}
     * @pre {@code role != null}
     * @post role has retrieved
     */
    private void sendRole(FirebaseUser user) throws NullPointerException {
        // Precondition testing
        // User precondition test
        if (user == null) {
            throw new NullPointerException("LoginData.sendRole.pre violated: user == null");
        }

        // Attempt to retrieve role
        FirebaseFirestore.getInstance()
            .collection("users").document(user.getUid())
            .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) { // Database read attempt was successful
                    // Variables
                    final DocumentSnapshot document = task.getResult();

                    if (document.exists()) { // User document was found
                        Log.d(TAG, "getUserRole:success"); // Log success

                        final String role = (String) document.get("role"); // Read role

                        receiver.signInSuccess(role); // Propagate success signal
                    } else { // User document could not be found or is empty
                        Log.w(TAG, "sendRole:document-failure", task.getException()); // Log failure
                        receiver.signInFail(); // Propagate document-failure signal
                    }
                } else { // Database read attempt failed
                    Log.w(TAG, "sendRole:database-read-failure", task.getException()); // Log failure
                    receiver.signInFail(); // Propagate database-read-failure signal
                }
            });
    }

    /**
     * Signs in users which are in an existing session.
     *
     * @post users which are in an existing session are signed in
     */
    public void signInSignedInUsers() {
        // Variables
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) { // User is still signed in
            sendRole(currentUser); // Get user role
        }
    }
}
