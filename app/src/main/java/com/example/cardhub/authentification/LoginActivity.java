package com.example.cardhub.authentification;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cardhub.R;
import com.example.cardhub.user_profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    LoginState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        state = new LoginState(this);

        // Instantiate layout components
        EditText et_email = findViewById(R.id.editText_email);
        EditText et_password = findViewById(R.id.editText_password);
        TextView tv_forgot = findViewById(R.id.textView_forgot);
        TextView tv_register = findViewById(R.id.textView_registerreferral);
        Button btn_login = findViewById(R.id.button_login);

        // Login Button is Pressed
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Instantiate variables
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                state.signIn(email, password);
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        state.getCurrentUser();
    }

    public void getCurrentUser(FirebaseUser user) {
        if (user != null) {
            // Open MapsActivity for a Collector or CreatorActivity for a Creator
            state.getUserRole(user);
        }
    }

    public void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void signInSuccess() {
        makeToast( "Login successful");
    }

    public void signInFail() {
        makeToast( "Login Fail");
    }
}