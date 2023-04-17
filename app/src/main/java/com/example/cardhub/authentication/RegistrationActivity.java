package com.example.cardhub.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.example.cardhub.R;
import com.example.cardhub.user_profile.CreatorProfileActivity;
import com.example.cardhub.user_profile.ProfileActivity;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Displays the Login View, and manages interactions.
 */
public class RegistrationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Instantiate layout components
        EditText et_registeremail = findViewById(R.id.editText_registeremail);
        EditText et_registerpassword = findViewById(R.id.editText_registerpassword);
        EditText et_confirm = findViewById(R.id.editText_confirm);
        CheckBox cb_tos = findViewById(R.id.checkBox_tos);
        TextView tv_loginreferral = findViewById(R.id.textView_signinreferral);
        Button btn_register = findViewById(R.id.button_register);
        TextView tv_tos = findViewById(R.id.textView_tos);
        Spinner spr_role = findViewById(R.id.spinner_role);

        // Registration variables
        RegistrationState state = new RegistrationState(this);

        // Create dropdown variables
        String[] roles = new String[]{"Card Collector", "Card Creator"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);

        // Set dropdown adapter
        spr_role.setAdapter(arrayAdapter);

        // Disable register button for TOS acceptance
        btn_register.setEnabled(false);

        // Make TOS clickable
        tv_tos.setText(Html.fromHtml(getString(R.string.terms_of_service_link), HtmlCompat.FROM_HTML_MODE_LEGACY));
        tv_tos.setClickable(true);
        tv_tos.setMovementMethod(LinkMovementMethod.getInstance());

        // TOS has been clicked
        cb_tos.setOnCheckedChangeListener((compoundButton, isChecked) -> btn_register.setEnabled(isChecked));

        // Register button was pressed
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Variables
                String email = et_registeremail.getText().toString();
                String password = et_registerpassword.getText().toString();
                String confirm = et_confirm.getText().toString();
                String role = spr_role.getSelectedItem().toString();

                state.register(email, password, confirm, role); // Pass login request

//                if (password.equals(confirm)) {
//                    state.register(email, password, role); // Pass login request
//                } else {
//                    Toast.makeText(RegistrationActivity.this, "Passwords don't match.", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        // Login link was clicked
        tv_loginreferral.setOnClickListener(view -> {
            // Open Login View
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });
    }

    /**
     * Open the correct start activity for the user depending on the {@code role}.
     *
     * @param role role used to decide which activity to open
     * @throws NullPointerException if {@code role == null}
     * @throws IllegalArgumentException if {@code !role.equals('Card Creator') && !role.equals('Card Collector')}
     */
    public void openStartActivity(@NonNull String role) throws NullPointerException, IllegalArgumentException {
        if (role == null) {
            throw new NullPointerException("RegistrationActivity.openStartActivity.pre violated: role == null");
        }

        // Variables
        Intent intent;

        if (role.equals("Card Collector")) {
            intent = new Intent(getApplicationContext(), ProfileActivity.class);
        } else if (role.equals("Card Creator")) {
            intent = new Intent(getApplicationContext(), CreatorProfileActivity.class);
        } else { // Given role is neither a Card Creator or Card Collector
            throw new IllegalArgumentException("RegistrationState.openActivity.pre violated: !role.equals('Card Creator') && !role.equals('Card Collector')");
        }

        // Empty activity memory
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Takes appropriate action upon the success of the registration.
     *
     * @param role role of the user that was registered
     */
    public void registrationSuccess(String role) {
        openStartActivity(role); // Open the appropriate activity
        // Display success message
        Toast.makeText(RegistrationActivity.this, "Registration has succeeded", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon the database-failure of the registration.
     */
    public void registrationDatabaseFail() {
        // Display database failure message
        Toast.makeText(RegistrationActivity.this, "Registration has failed, please try again later", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon the confirmation-failure of the registration.
     */
    public void registrationConfirmationFail() {
        // Display confirmation failure message
        Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon the database-failure of the registration.
     */
    public void registrationPasswordFail() {
        // Display password failure message
        Toast.makeText(RegistrationActivity.this, "Password length must be at least 6 characters", Toast.LENGTH_LONG).show();
    }
}