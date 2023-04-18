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
 * Display the ForgotPassword View, and manages interactions.
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
        EditText et_forgot_password = findViewById(R.id.editText_email_forgot);
        Button btn_forgot_password = findViewById(R.id.button_email_forgotten);
        TextView tv_login_referral = findViewById(R.id.textView_loginreferral_forgot);

        // Sign-in variables
        ForgotPasswordState state = new ForgotPasswordState(this);

        // Forgot password link was clicked
        btn_forgot_password.setOnClickListener(view -> {
            // Variables
            String emailAddress = et_forgot_password.getText().toString();

            state.sendForgotPasswordEmail(emailAddress); // Pass password-reset-mail request
        });

        // Login link was clicked
        tv_login_referral.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
    }

    /**
     * Takes appropriate action upon the success of sending the password-reset-email.
     */
    public void sendForgotPasswordEmailSuccess() {
        // Start Login Activity
        // Variables
        Intent intent = new Intent(this.getApplicationContext(), ProfileActivity.class);
        // Remove activities from memory
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent); // Open activity

        // Display success message
        Toast.makeText(ForgotPasswordActivity.this, "Reset request was send successfully", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon the failure of sending the password-reset-email.
     */
    public void sendForgotPasswordEmailFailure() {
        // Display failure message
        Toast.makeText(ForgotPasswordActivity.this, "Reset request failed", Toast.LENGTH_LONG).show();
    }
}