package com.example.cardhub;

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
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

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

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(LoginActivity.this, "Authentication succeeded.",
                                            Toast.LENGTH_SHORT).show();

                                    // Move to next screen
                                    openStartActivity(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
        user = mAuth.getCurrentUser();
        if (user != null) {
            // Open MapsActivity for a Collector or CreatorActivity for a Creator
            openStartActivity(user);
        }
    }

    private void openStartActivity(FirebaseUser currentUser) {
        db = FirebaseFirestore.getInstance();
        String uid = currentUser.getUid();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Read data
                        String role = (String) document.get("role");
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        if (role.equals("Card Collector")) {
                            // Move to next screen
                            intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        } else if (role.equals("Card Creator")) {
                            intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        }

                        // Remove returnal activities from memory
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "No account!");
                        Toast.makeText(LoginActivity.this, "No account!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}