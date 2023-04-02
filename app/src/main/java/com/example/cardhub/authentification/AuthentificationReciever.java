package com.example.cardhub.authentification;

import com.google.firebase.auth.FirebaseUser;

public interface AuthentificationReciever {

    public void signInSuccess(FirebaseUser user);
    public void signInFail();
    public void userRoleCallback(String role);
    public void recieveCurrentUser(FirebaseUser user);

}
