package com.example.cardhub.authentication;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Design Pattern Implementation for the registration of users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class RegistrationData {
    // Constants
    public static final String TAG = "RegistrationData";
    private final RegisterReceiver receiver;

    /**
     * Constructs a new RegistrationData instance using the given {@code receiver}
     * instance.
     *
     * @param receiver given receiver instance
     * @pre {@code receiver != null}
     * @throws NullPointerException if {@code receiver == null}
     * @post instance is initialized
     */
    public RegistrationData(RegisterReceiver receiver) throws NullPointerException {
        // Precondition testing
        // Receiver precondition test
        if (receiver == null) {
            throw new NullPointerException(
                    "RegistrationData.RegistrationData.pre violated: receiver == null"
            );
        }

        this.receiver = receiver;
    }

    /**
     * Registers an account using the given {@code email}, {@code password}, and {@code role}.
     *
     * @param email email to register with
     * @param password password to register with
     * @param role role to register with
     * @pre {@code email != null && password != null && role != null}
     * @throws NullPointerException if {@code email != null || password != null || role != null}
     * @post new account has been registered
     */
    public void register(String email, String password, String role) throws NullPointerException {
        // Precondition testing
        // Email precondition test
        if (email == null) {
            throw new NullPointerException("RegistrationData.signIn.pre violated: email == null");
        }

        // Password precondition test
        if (password == null) {
            throw new NullPointerException("RegistrationData.signIn.pre violated: password == null");
        }

        // Role precondition test
        if (role == null) {
            throw new NullPointerException("RegistrationData.signIn.pre violated: role == null");
        }

        // Variables
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Create account
        mAuth.createUserWithEmailAndPassword(email, password)
           .addOnCompleteListener(task -> {
               if (task.isSuccessful()) { // Registration succeeded
                   // Log success
                   Log.d(TAG, "signUpWithEmail:success");

                   // Variables
                   FirebaseUser user = mAuth.getCurrentUser();
                   Map<String, Object> userEntry = createNewUserEntry(role);

                   // Add the userEntry to the database
                   uploadUserEntry(user, userEntry, role);
               } else { // Registration failed
                   // Log failure
                   Log.w(TAG, "signUpWithEmail:failure", task.getException());
                   receiver.registrationDatabaseFail(); // Propagate database-failure
               }
           });
    }

    /**
     * Uploads the given {@code userEntry}.
     *
     * @param user user for which the userEntry is being uploaded
     * @param userEntry entry that is going to be uploaded
     * @param role role of the user
     * @pre {@code user != null}
     * @throws NullPointerException if {@code user == null}
     * @post userEntry has been uploaded
     */
    private void uploadUserEntry(FirebaseUser user, Map<String, Object> userEntry, String role) throws NullPointerException {
        // Precondition testing
        // User precondition test
        if (user == null) {
            throw new NullPointerException("RegistrationData.uploadUserEntry.pre violated: user == null");
        }

        // Attempt to upload the userEntry
        FirebaseFirestore.getInstance()
            .collection("users").document(user.getUid())
            .set(userEntry)
            .addOnSuccessListener(aVoid -> { // Upload succeeded
                // Log success
                Log.d(TAG, "userEntryUpload:success");
                receiver.registrationSuccess(role); // Propagate success signal
            })
            .addOnFailureListener(e -> { // Upload failed
                // Log failure
                Log.e(TAG, "userEntryUpload:failure", e);
                receiver.registrationDatabaseFail(); // Propagate database-failure signal
            });
    }

    /**
     * Creates a new {@code userEntry} HashMap using the given {@code role}.
     *
     * @param role role of the user for which the userEntry is generated
     * @pre {@code role.equals("Card Collector") ^ role.equals(Card Creator)}
     * @throws IllegalArgumentException if {@code !(role.equals("Card Collector") ^ role.equals("Card Creator"))}
     * @return HashMap with the appropriate user characteristics for Firestore
     */
    @NonNull
    private Map<String, Object> createNewUserEntry(String role) throws NullPointerException, IllegalArgumentException {
        // Variables
        Map<String, Object> userEntry = new HashMap<>();

        // HashMap Content
        userEntry.put("role", role); // Default user characteristic

        if (role.equals("Card Collector")) {
            userEntry.put("tradesmade", 0); // Mandatory Card Collector Statistic
        } else if (!role.equals("Card Creator")) { // Role precondition test
            throw new IllegalArgumentException("RegistrationData.createNewUserEntry.pre violated: role == " + role);
        }

        return userEntry;
    }
}
