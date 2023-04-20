package com.example.cardhub.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cardhub.R;
import com.example.cardhub.user_profile.CreatorProfileActivity;
import com.example.cardhub.user_profile.ProfileActivity;

/**
 * Displays the Login View, and manages interactions.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class LoginActivity extends AppCompatActivity {
    // Constants
    static final Class<?> START_ACTIVITY_COLLECTOR = ProfileActivity.class;
    static final Class<?> START_ACTIVITY_CREATOR = CreatorProfileActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instantiate layout components
        final EditText et_email = findViewById(R.id.editText_email);
        final EditText et_password = findViewById(R.id.editText_password);
        final TextView tv_forgot = findViewById(R.id.textView_forgot);
        final TextView tv_register = findViewById(R.id.textView_registerreferral);
        final Button btn_login = findViewById(R.id.button_login);

        // Login button was pressed
        btn_login.setOnClickListener(view -> {
            // Variables
            final String email = et_email.getText().toString();
            final String password = et_password.getText().toString();
            final LoginState state = new LoginState(this);

            state.signIn(email, password); // Pass sign-in request
        });

        // Registration link was clicked
        tv_register.setOnClickListener(view -> {
            // Open Registration View
            startActivity(new Intent(this, RegistrationActivity.class));
        });

        // Forgot password link was clicked
        tv_forgot.setOnClickListener(view -> {
            // Open ForgotPassword View
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Attempt to sign-in in signed in users
        // Variables
        final LoginState state = new LoginState(this);

        state.signInSignedInUsers(); // Pass session-check request
    }

    /**
     * Takes appropriate action upon failing to sign-in due to database issues.
     */
    public void signInDatabaseFail() {
        // Display failure message
        Toast.makeText(this, "Failed to sign-in", Toast.LENGTH_SHORT).show();
    }

    /**
     * Takes appropriate action upon signing-in as a Card Collector.
     */
    public void signInSuccessCollector() {
        openStartActivity(START_ACTIVITY_COLLECTOR); // Open the appropriate activity
        // Display collector-success message
        Toast.makeText(this, "Signed-in as Card Collector", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon signing-in as a Card Creator.
     */
    public void signInSuccessCreator() {
        openStartActivity(START_ACTIVITY_CREATOR); // Open the appropriate activity
        // Display creator-success message
        Toast.makeText(this, "Signed-in as Card Creator", Toast.LENGTH_LONG).show();
    }

    /**
     * Open the correct start activity for the user depending on the {@code role}.
     *
     * @param cls component class that is used to decide which activity to open
     * @throws NullPointerException if {@code cls == null}
     * @pre {@code cls != null}
     * @post the given {@code cls} specified activity has been opened and the activity memory has been wiped
     */
    private void openStartActivity(Class<?> cls) throws NullPointerException {
        // Precondition testing
        // Cls testing
        if (cls == null) {
            throw new NullPointerException("LoginActivity.openStartActivity.pre violated: cls == null");
        }

        // Variables
        final Intent intent = new Intent(this, cls);

        // Empty activity memory
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent); // Open activity
    }

    /**
     * Takes appropriate action upon failing to sign-in due to password-string issues.
     */
    public void signInPasswordStringFail() {
        // Display password-string-failure message
        Toast.makeText(this, "Fill in a password", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon failing to sign-in due to email-string issues.
     */
    public void signInEmailStringFail() {
        // Display email-string-failure message
        Toast.makeText(this, "Fill in an email", Toast.LENGTH_LONG).show();
    }
}