package com.example.cardhub.authentification;

import com.google.firebase.auth.FirebaseUser;

public interface RegisterReceiver {
    public void registrationSuccess(FirebaseUser user, String role);
    public void registrationFail();
}
