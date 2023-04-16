package com.example.cardhub.authentication;

import android.content.Intent;

import com.example.cardhub.user_profile.CreatorProfileActivity;
import com.example.cardhub.user_profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginState implements LoginReceiver {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private LoginData data;
    private FirebaseUser user;
    private LoginActivity activity;

    public LoginState(LoginActivity activity) {
        data = new LoginData(this);
        this.activity = activity;
    }

    @Override
    public void signInSuccess(FirebaseUser user) {
        this.user = user;
        getUserRole(user);
        activity.signInSuccess();
    }

    public void signIn(String email, String password) {
        data.signIn(email, password);
    }

    public void getCurrentUser() {
        data.getCurrentUser();
    }

    public void getUserRole(FirebaseUser user) {
        data.getUserRole(user);
    }

    @Override
    public void signInFail() {
        activity.signInFail();
    }

    @Override
    public void userRoleCallback(String role) {
        Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
        if (role.equals("Card Collector")) {
            // Move to next screen
            intent = new Intent(activity.getApplicationContext(), ProfileActivity.class);
        } else if (role.equals("Card Creator")) {
            intent = new Intent(activity.getApplicationContext(), CreatorProfileActivity.class);
        }

        // Remove returnal activities from memory
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void receiveCurrentUser(FirebaseUser user) {
        activity.getCurrentUser(user);
    }
}
