package com.example.cardhub;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Instantiate layout components
        EditText et_registeremail = findViewById(R.id.editText_registeremail);
        EditText et_registerpassword = findViewById(R.id.editText_registerpassword);
        EditText et_confirm = findViewById(R.id.editText_confirm);
        CheckBox cb_tos = findViewById(R.id.checkBox_tos);
        TextView tv_loginreferral = findViewById(R.id.textView_signinreferral);
        Button btn_register = findViewById(R.id.button_register);
        TextView tv_tos = findViewById(R.id.textView_tos);

        // TOS disables btn
        btn_register.setEnabled(false);

        // Make Terms of Service clickable
        tv_tos.setText(Html.fromHtml("I have read and agree to the <br> <a href='https://www.youtube.com/watch?v=xvFZjo5PgG0'>Terms and Conditions</a>", HtmlCompat.FROM_HTML_MODE_LEGACY));
        tv_tos.setClickable(true);
        tv_tos.setMovementMethod(LinkMovementMethod.getInstance());

        // TOS are agreed to
        cb_tos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                btn_register.setEnabled(b);
            }
        });

        // Register button was pressed
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Instantiate variables
                String email = et_registeremail.getText().toString();
                String password = et_registerpassword.getText().toString();
                String confirm = et_confirm.getText().toString();

                if (password.equals(confirm)) {
                    // Create account
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signUpWithEmail:success");
                                        Toast.makeText(RegistrationActivity.this, "Registration succeeded.",
                                                Toast.LENGTH_SHORT).show();
                                        // Move to next screen
                                        Intent intent = new Intent(getApplicationContext(), MapActivity.class);

                                        // Ensure no returns to registration screen
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        // If sign up fails, display a message to the user.
                                        Log.w(TAG, "signUpWithEmail:failure", task.getException());
                                        Toast.makeText(RegistrationActivity.this, "Registration failed, try again later!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegistrationActivity.this, "Registration failed, the passwords differ!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_loginreferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}