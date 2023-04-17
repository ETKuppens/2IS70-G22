package com.example.cardhub.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    private LoginState state;

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

        // Login button is pressed
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Instantiate variables
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                state.signIn(email, password); // Pass authentication request
            }
        });

        // Registration link is pressed
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open RegistrationActivity
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        // Forgotten password link is pressed
        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open ForgotPasswordActivity
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

    /**
     * Displays the given {@code msg} as a toast.
     *
     * @param msg message to be displayed on the toast
     * @pre {@code msg != null}
     * @throws NullPointerException if {@code msg == null}
     */
    private void makeToast(String msg) {
        if (msg == null) {
            throw new NullPointerException("LoginActivity.makeToast.pre violated: msg == null");
        }

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a success toast.
     */
    public void signInSuccess() {
        makeToast( "Login successful");
    }

    /**
     * Displays a failure toast.
     */
    public void signInFail() {
        makeToast( "Login Fail");
    }
}