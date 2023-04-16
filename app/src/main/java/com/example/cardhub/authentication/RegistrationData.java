package com.example.cardhub.authentication;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Design Pattern Implementation for the registering of users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class RegistrationData {
    // Constants
    public static final String TAG = "RegistrationData";

    // Variables
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RegisterReceiver receiver;

    /**
     * Constructs a new RegistrationData instance using the given {@code receiver}
     * instance.
     *
     * @param receiver given receiver instance
     *
     * @pre {@code receiver != null}
     *
     * @throws NullPointerException if {@code receiver == null}
     */
    public RegistrationData(RegisterReceiver receiver) throws NullPointerException {
        if (receiver == null) {
            throw new NullPointerException(
                    "RegistrationData.RegistrationData.pre violated: receiver == null"
            );
        }

        this.receiver = receiver;
    }

    /**
     * Registers the client using the given {@code email}, and {@code password}
     * with the {@code role} role.
     *
     * @param email email to register with
     * @param password password to register with
     * @param role role to register with
     *
     * @pre {@code email != null && password != null && role != null}
     *
     * @throws NullPointerException if {@code email != null || password != null || role != null}
     */
    public void register(String email, String password, String role) throws NullPointerException {
        if (email == null) {
            throw new NullPointerException("RegistrationData.signIn.pre violated: email == null");
        }

        if (password == null) {
            throw new NullPointerException("RegistrationData.signIn.pre violated: password == null");
        }

        if (role == null) {
            throw new NullPointerException("RegistrationData.signIn.pre violated: role == null");
        }

        // Create account
        mAuth.createUserWithEmailAndPassword(email, password)
           .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()) { // Client has been registered successfully
                       // Log success
                       Log.d(TAG, "signUpWithEmail:success");
                       // Variables
                       FirebaseUser user = mAuth.getCurrentUser();
                       String uid = user.getUid();
                       Map<String, Object> userEntry = createNewUserEntry(role);

                       // Add the userEntry to the database
                       uploadUserEntry(uid, userEntry, role);
                   } else { // Client has not been registered successfully
                       // Log failure
                       Log.w(TAG, "signUpWithEmail:failure", task.getException());
                       receiver.registrationFail(); // Display error message
                   }
               }
           });
    }

    /**
     * Uploads the given {@code userEntry}.
     *
     * @param uid uid of the user for which the userEntry is being uploaded
     * @param userEntry entry that is going to be uploaded
     * @param role role of the user
     */
    @NonNull
    private void uploadUserEntry(String uid, Map<String, Object> userEntry, String role) {
        db.collection("users").document(uid)
            .set(userEntry)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "userEntryUpload:success");
                    receiver.registrationSuccess(role);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Log failure
                    Log.e(TAG, "userEntryUpload:failure", e);
                    receiver.registrationFail(); // Display error message
                }
            });
    }

    /**
     * Creates a new {@code userEntry} HashMap using the given {@code role}.
     *
     * @param role role of the user for which the userEntry is generated
     *
     * @pre {@code role.equals("Card Collector") ^ role.equals(Card Creator)}
     *
     * @throws NullPointerException if {@code role == null}
     * @throws IllegalArgumentException if {@code !role.equals("Card Collector") && !role.equals("Card Creator")}
     */
    @NonNull
    private Map<String, Object> createNewUserEntry(String role) throws NullPointerException, IllegalArgumentException {
        if (role == null) {
            throw new NullPointerException("RegistrationData.createNewUserEntry.pre violated: role == null");
        }

        // Variables
        Map<String, Object> userEntry = new HashMap<>();

        // HashMap Content
        userEntry.put("role", role); // Default user characteristic

        if (role.equals("Card Collector")) {
            userEntry.put("tradesmade", 0); // Mandatory Card Collector Statistic
        } else if (!role.equals("Card Creator")) {
            throw new IllegalArgumentException("RegistrationData.createNewUserEntry.pre violated: !role.equals('Card Collector') && !role.equals('Card Creator')");
        }

        return userEntry;
    }
}
