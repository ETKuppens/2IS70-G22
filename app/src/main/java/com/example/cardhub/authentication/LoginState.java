package com.example.cardhub.authentication;

import android.content.Intent;

import com.example.cardhub.user_profile.CreatorProfileActivity;
import com.example.cardhub.user_profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * State Design Pattern implementation for the signing in of users.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class LoginState implements LoginReceiver {
    // Variables
    private static FirebaseFirestore db;
    private static LoginActivity activity;
    private LoginData data;
    private FirebaseUser user;

    /**
     * Constructs a new LoginState instance using the given {@code activity}
     * instance.
     *
     * @param activity LoginActivity instance to be referenced in the code
     *
     * @pre {@code activity != null}
     * @throws NullPointerException if {@code activity == null}
     */
    public LoginState(LoginActivity activity) throws NullPointerException {
        if (activity == null) {
            throw new NullPointerException(
                    "LoginState.LoginState.pre violated: activity == null"
            );
        }

        this.activity = activity;
        this.data = new LoginData(this);
    }

    /**
     * Passes the signing in success signal to the {@code activity}.
     *
     * @param user user account that has been signed in to successfully
     *
     * @pre {@code activity != null && user != null}
     *
     * @throws NullPointerException if {@code activity == null || user == null}
     */
    @Override
    public void signInSuccess(FirebaseUser user) throws NullPointerException {
        if (activity == null) {
            throw new NullPointerException("LoginState.signInSuccess violated: activity == null");
        }

        this.user = user;

        getUserRole(user); // Pass role request to data

        activity.signInSuccess(); // Activity Callback
    }

    /**
     * Passes the sign in call to the Data Design Pattern class.
     *
     * @param email email to sign in with
     * @param password password to sign in with
     *
     * @pre {@code email != null && password != null}
     *
     * @throws NullPointerException if {email == null || password == null}
     */
    public void signIn(String email, String password) throws NullPointerException {
        if (email == null) {
            throw new NullPointerException("LoginState.signIn.pre violated: email == null");
        }

        if (password == null) {
            throw new NullPointerException("LoginState.signIn.pre violated: password == null");
        }

        if (data == null) {
            throw new NullPointerException("LoginState.signIn.pre violated: data == null");
        }

        data.signIn(email, password); // Data Authentication
    }

    /**
     * Passes the user retrieval to the Data Design Pattern.
     *
     * @pre {@code data != null}
     *
     * @throws if {@code data == null}
     */
    public void getCurrentUser() throws NullPointerException {
        if (data == null) {
            throw new NullPointerException("LoginState.getCurrentUser.pre violated: data == null");
        }

        data.getCurrentUser(); // User retrieval
    }

    /**
     * Passes the role retrieval to the Data Design Pattern.
     *
     * @param user user to be checked on role
     *
     * @pre {@code data != null && user != null}
     *
     * @throws NullPointerException if {data == null || user == null}
     */
    public void getUserRole(FirebaseUser user) throws NullPointerException {
        if (data == null) {
            throw new NullPointerException("LoginState.getUserRole.pre violated: data == null");
        }

        if (user == null) {
            throw new NullPointerException("LoginState.getuserRole.pre violated: user == null");
        }

        data.getUserRole(user); // Role retrieval
    }

    /**
     * Passes the signing in failure to the {@code activity}
     *
     * @pre {@code activity != null}
     *
     * @throw NullPointerExcpetion if {@code activity == null}
     */
    @Override
    public void signInFail() throws NullPointerException {
        if (activity == null) {
            throw new NullPointerException("LoginState.signInFail.pre violated: activity == null");
        }

        activity.signInFail(); // Sign in failure report
    }

    /**
     * Open the appropriate start activity based on the passed {@code role}.
     *
     * @param role role of the user
     *
     * @pre {@code (role == "Card Collector" || role == "Card Creator") && activity != null}
     *
     * @throws NullPointerException if {@code role == null || activity == null}
     * @throws IllegalArgumentException if {@code role != "Card Collector" && role != "Card Creator"}
     */
    @Override
    public void userRoleCallback(String role) throws NullPointerException, IllegalArgumentException {
        if (role == null) {
            throw new NullPointerException("LoginState.userRoleCallback.pre violated: role == null");
        }

        if (activity == null) {
            throw new NullPointerException("LoginState.userRoleCallback.pre violated: activity == null");
        }

        // Variables
        Intent intent;

        // Select the correct start screen
        if (role.equals("Card Collector")) {
            intent = new Intent(activity.getApplicationContext(), ProfileActivity.class);
        } else if (role.equals("Card Creator")) {
            intent = new Intent(activity.getApplicationContext(), CreatorProfileActivity.class);
        } else {
            throw new IllegalArgumentException("LoginState.userRoleCallback.pre violated: role != 'Card Collector' && role != 'Card Creator'");
        }

        // Remove activities from memory
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent); // Update UI
    }

    /**
     * Passes {@code user} to the {@code activity} instance.
     *
     * @param user given user account
     *
     * @pre {@code user != null && activity != null}
     *
     * @throws NullPointerException if {@code user == null || activity == null}
     */
    @Override
    public void receiveCurrentUser(FirebaseUser user) throws NullPointerException {
        if (user == null) {
            throw new NullPointerException("LoginState.receiveCurrentUser.pre violated: user == null");
        }

        if (activity == null) {
            throw new NullPointerException("LoginState.receiveCurrentUser.pre violated: activity == null");
        }

        activity.getCurrentUser(user); // User retrieval
    }
}
