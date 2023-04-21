package com.example.cardhub.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cardhub.R;
import com.example.cardhub.user_profile.ProfileActivity;

/**
 * Displays the ForgotPassword View, and manages interactions.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Instantiate layout components
        final EditText et_forgot_password = findViewById(R.id.editText_email_forgot);
        final Button btn_forgot_password = findViewById(R.id.button_email_forgotten);
        final TextView tv_login_referral = findViewById(R.id.textView_loginreferral_forgot);

        // Sign-in variables
        final ForgotPasswordState state = new ForgotPasswordState(this);

        // Forgot password button was pressed
        btn_forgot_password.setOnClickListener(view -> {
            // Variables
            final String emailAddress = et_forgot_password.getText().toString();

            state.sendForgotPasswordEmail(emailAddress); // Pass password-reset-mail request
        });

        // Login link was clicked
        tv_login_referral.setOnClickListener(view -> {
            // Open Login View
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    /**
     * Takes appropriate action upon the success of sending the password-reset-email.
     */
    public void sendForgotPasswordEmailSuccess() {
        // Start Login Activity
        // Variables
        final Intent intent = new Intent(this, ProfileActivity.class);
        // Remove activities from memory
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent); // Open activity

        // Display success message
        Toast.makeText(ForgotPasswordActivity.this, "Reset request was send successfully", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon the database-failure of sending the password-reset-email.
     */
    public void sendForgotPasswordEmailDatabaseFailure() {
        // Display failure message
        Toast.makeText(ForgotPasswordActivity.this, "Failed to send a password reset request", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon the email-string-failure of sending the password-reset-email.
     */
    public void sendForgotPasswordEmailStringFailure() {
        // Display email-string-failure message
        Toast.makeText(this, "Fill in an email", Toast.LENGTH_LONG).show();
    }
}