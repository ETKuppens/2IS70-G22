package com.example.cardhub.user_profile;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileData {
    public void logOut() {
        FirebaseAuth.getInstance().signOut();
    }
}
