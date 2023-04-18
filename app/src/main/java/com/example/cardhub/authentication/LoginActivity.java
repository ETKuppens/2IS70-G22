package com.example.cardhub.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cardhub.R;

/**
 * Displays the Login View, and manages interactions.
 */
public class LoginActivity extends AppCompatActivity {
    // Variables
    private final LoginState state = new LoginState(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instantiate layout components
        EditText et_email = findViewById(R.id.editText_email);
        EditText et_password = findViewById(R.id.editText_password);
        TextView tv_forgot = findViewById(R.id.textView_forgot);
        TextView tv_register = findViewById(R.id.textView_registerreferral);
        Button btn_login = findViewById(R.id.button_login);

        // Login button was pressed
        btn_login.setOnClickListener(view -> {
            // Variables
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();

            state.signIn(email, password); // Pass sign in request
        });

        // Registration link was clicked
        tv_register.setOnClickListener(view -> {
            // Open RegistrationActivity
            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
        });

        // Forgot password link was clicked
        tv_forgot.setOnClickListener(view -> {
            // Open ForgotPasswordActivity
            startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        state.checkCurrentUser(); // Pass user check
    }

    /**
     * Takes appropriate action upon succeeding with signing-in.
     */
    public void signInSuccess() {
        // Display success message
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
    }

    /**
     * Takes appropriate action upon failing to sign-in.
     */
    public void signInFail() {
        // Display failure message
        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
    }
}