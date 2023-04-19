package com.example.cardhub.authentification;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class RegistrationState implements RegisterReceiver {
    private final RegistrationData data;
    private FirebaseUser user;
    private final RegistrationActivity activity;

    public RegistrationState(RegistrationActivity activity)  {
        this.activity = activity;
        this.data = new RegistrationData(this);
    }

    public void register(String email, String password, String role) {
        data.register(email, password, role);
    }


    @Override
    public void registrationSuccess(FirebaseUser user, String role) {
        this.user = user;
        activity.openStartActivity(role);
    }

    @Override
    public void registrationFail() {
        Toast.makeText(activity.getApplicationContext(), "Registration failed, try again later!",
                Toast.LENGTH_SHORT).show();

    }
}
