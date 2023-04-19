package com.example.cardhub.user_profile;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * Interact with Firebase to get profile information.
 */
public class ProfileData {
    FirebaseFirestore db;
    FirebaseAuth auth;
    ProfileRepository repo;

    public ProfileData(ProfileRepository repo, FirebaseAuth auth, FirebaseFirestore db) {
        this.db = db;
        this.auth = auth;
        this.repo = repo;
    }

    /**
     * Log the current user out.
     */
    public void logOut() {
        auth.signOut();
    }

    /**
     * Get profile information from the current user.
     */
    public void requestProfile() {
        db.collection("users/" + auth.getUid() + "/cards").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    int cardAmount = task.getResult().size();

                    db.document("users/" + auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && auth.getCurrentUser() != null) {
                                Map<String, Object> profile = new HashMap<>();
                                profile.put("email", auth.getCurrentUser().getEmail());
                                profile.put("cardamount", cardAmount);
                                profile.put("tradesmade", (int)(long)task.getResult().get("tradesmade"));
                                repo.receiverProfile(profile);
                            }
                        }
                    });

                }
                else {

                    Map<String, Object> profile = new HashMap<>();
                    profile.put("email", auth.getCurrentUser().getEmail());
                    profile.put("cardamount", 0);
                    profile.put("tradesmade", 0);

                    repo.receiverProfile(profile);
                }
            }

        });
    }
}
