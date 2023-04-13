package com.example.cardhub.authentification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.example.cardhub.R;
import com.example.cardhub.user_profile.ProfileActivity;


public class RegistrationActivity extends AppCompatActivity {

    RegistrationState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        state = new RegistrationState(this);


        // Instantiate layout components
        EditText et_registeremail = findViewById(R.id.editText_registeremail);
        EditText et_registerpassword = findViewById(R.id.editText_registerpassword);
        EditText et_confirm = findViewById(R.id.editText_confirm);
        CheckBox cb_tos = findViewById(R.id.checkBox_tos);
        TextView tv_loginreferral = findViewById(R.id.textView_signinreferral);
        Button btn_register = findViewById(R.id.button_register);
        TextView tv_tos = findViewById(R.id.textView_tos);
        Spinner spr_role = findViewById(R.id.spinner_role);

        // Create dropdown list
        String[] roles = new String[]{"Card Collector", "Card Creator"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        spr_role.setAdapter(arrayAdapter);

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
                String role = spr_role.getSelectedItem().toString();

                if (password.equals(confirm)) {
                    state.register(email, password, confirm, role);
                } else {
                    Toast.makeText(RegistrationActivity.this, "Passwords don't match.", Toast.LENGTH_SHORT).show();
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

    //private void openStartActivity(FirebaseUser currentUser) {
    //    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
    //        @Override
    //        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
    //            if (task.isSuccessful()) {
    //                DocumentSnapshot document = task.getResult();
    //                if (document.exists()) {
    //                    // Read data
    //                    String role = (String) document.get("role");
    //                    Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
    //                    if (role.equals("Card Collector")) {
    //                        // Move to next screen
    //                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
    //                    }
    //                    else if (role.equals("Card Creator")) {
    //                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
    //                    }

    //                    // Remove returnal activities from memory
    //                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    //                    startActivity(intent);
    //                } else {
    //                    Log.d(TAG, "No account!");
    //                    Toast.makeText(RegistrationActivity.this, "No account!", Toast.LENGTH_SHORT).show();
    //                }
    //            } else {
    //                Log.d(TAG, "get failed with ", task.getException());
    //            }
    //        }
    //    });
    //}

    public void openStartActivity() {
        Toast.makeText(this, "Registration succeeded.",
                Toast.LENGTH_SHORT).show();
        // Move to next screen
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);

        // Ensure no returns to registration screen
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}