package com.example.cardhub.authentification;

import static android.content.ContentValues.TAG;

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

public class RegistrationData {
    FirebaseAuth mAuth;
    RegisterReceiver receiver;
    FirebaseFirestore db;
    RegistrationState state;

    public RegistrationData(RegisterReceiver receiver) {
        this.mAuth = FirebaseAuth.getInstance();
        this.receiver = receiver;
        this.db = FirebaseFirestore.getInstance();
    }

    public void register(String email, String password, String confirm, String role) {
            // Create account
            mAuth.createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           // Upload role to users collection
                           FirebaseUser user = mAuth.getCurrentUser();
                           String uid = user.getUid();
                           Map<String, Object> userEntry = new HashMap<>();
                           userEntry.put("role", role);

                           // Add a new document with a generated ID
                           db.collection("users").document(uid)
                                   .set(userEntry)
                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           Log.d("FIRESTORE", "DocumentSnapshot successfully written!");
                                           receiver.registrationSuccess(mAuth.getCurrentUser());
                                       }
                                   })
                                   .addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Log.w("FIRESTORE", "Error writing document", e);
                                           receiver.registrationFail();
                                       }
                                   });

                           // Sign in success, update UI with the signed-in user's information
                           Log.d(TAG, "signUpWithEmail:success");
                       } else {
                           // If sign up fails, display a message to the user.
                           Log.w(TAG, "signUpWithEmail:failure", task.getException());
                           receiver.registrationFail();
                       }
                   }
               });
    }
}
