package com.example.cardhub.authentification;

import android.content.Intent;
import android.widget.Toast;

import com.example.cardhub.user_profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationState implements RegisterReceiver {
    private final RegistrationData data;
    private FirebaseUser user;
    private final RegistrationActivity activity;

    public RegistrationState(RegistrationActivity activity)  {
        this.activity = activity;
        this.data = new RegistrationData(this);
    }

    public void register(String email, String password, String confirm, String role) {
        data.register(email, password, confirm, role);
    }


    @Override
    public void registrationSuccess(FirebaseUser user) {
        this.user = user;
        activity.openStartActivity();
    }

    @Override
    public void registrationFail() {
        Toast.makeText(activity.getApplicationContext(), "Registration failed, try again later!",
                Toast.LENGTH_SHORT).show();

    }
}
