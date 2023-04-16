package com.example.cardhub.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cardhub.R;
import com.example.cardhub.user_profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Display the ForgotPassword View, and manages interactions.
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    // Variables
    private ForgotPasswordState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Firebase Authentication
        state = new ForgotPasswordState(this);

        // Instantiate layout components
        EditText et_forgotpassword = findViewById(R.id.editText_email_forgot);
        Button btn_forgotpassword = findViewById(R.id.button_email_forgotten);
        TextView tv_loginreferral = findViewById(R.id.textView_loginreferral_forgot);

        // Reset pressed
        btn_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Variables
                String emailAddress = et_forgotpassword.getText().toString();

                state.sendForgotPasswordEmail(emailAddress); // Pass password reset request
            }
        });

        // Login link was clicked
        tv_loginreferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    /**
     * Takes appropriate action upon the success of the password-reset request.
     */
    public void sendForgotPasswordEmailSuccess() {
        // Start Login Activity
        Intent intent = new Intent(this.getApplicationContext(), ProfileActivity.class);
        // Remove activities from memory
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent); // Update UI

        // Display success message
        Toast.makeText(ForgotPasswordActivity.this, "Reset request was send successfully", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon the failure of the password-reset request.
     */
    public void sendForgotPasswordEmailFailure() {
        // Display failure message
        Toast.makeText(ForgotPasswordActivity.this, "Reset request failed", Toast.LENGTH_LONG).show();
    }
}