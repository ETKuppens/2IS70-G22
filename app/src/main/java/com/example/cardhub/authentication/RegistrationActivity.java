package com.example.cardhub.authentication;

import static com.example.cardhub.authentication.LoginActivity.START_ACTIVITY_COLLECTOR;
import static com.example.cardhub.authentication.LoginActivity.START_ACTIVITY_CREATOR;
import static com.example.cardhub.authentication.RegistrationState.PASSWORD_LENGTH;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

/**
 * Displays the Login View, and manages interactions.
 *
 * @author  Vladislav Budiak, Sevket Tulgar Dinc, Etienne Kuppens,
 *          Aqiel Oostenbrug, Marios Papalouka, Rijkman Pilaar
 * @groupname Group 22
 * @date 16-04-2023
 */
public class RegistrationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Instantiate layout components
        final EditText et_register_email = findViewById(R.id.editText_registeremail);
        final EditText et_register_password = findViewById(R.id.editText_registerpassword);
        final EditText et_confirm = findViewById(R.id.editText_confirm);
        final CheckBox cb_tos = findViewById(R.id.checkBox_tos);
        final TextView tv_login_referral = findViewById(R.id.textView_signinreferral);
        final Button btn_register = findViewById(R.id.button_register);
        final TextView tv_tos = findViewById(R.id.textView_tos);
        final Spinner spr_role = findViewById(R.id.spinner_role);

        // Registration variables
        final RegistrationState state = new RegistrationState(this);

        // Create dropdown variables
        final String[] roles = new String[]{"Card Collector", "Card Creator"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);

        // Set dropdown adapter
        spr_role.setAdapter(arrayAdapter);

        // Disable register button for TOS acceptance
        btn_register.setEnabled(false);

        // Make TOS clickable
        tv_tos.setText(Html.fromHtml(getString(R.string.terms_of_service_link), HtmlCompat.FROM_HTML_MODE_LEGACY));
        tv_tos.setClickable(true);
        tv_tos.setMovementMethod(LinkMovementMethod.getInstance());

        // TOS link was clicked
        cb_tos.setOnCheckedChangeListener((compoundButton, isChecked) -> btn_register.setEnabled(isChecked));

        // Register button was pressed
        btn_register.setOnClickListener(view -> {
            // Variables
            final String email = et_register_email.getText().toString();
            final String password = et_register_password.getText().toString();
            final String confirm = et_confirm.getText().toString();
            final String role = spr_role.getSelectedItem().toString();

            state.register(email, password, confirm, role); // Pass login request
        });

        // Login link was clicked
        tv_login_referral.setOnClickListener(view -> {
            // Open Login View
            startActivity(new Intent(this, LoginActivity.class));
        });
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
            throw new NullPointerException("RegistrationActivity.openStartActivity.pre violated: cls == null");
        }

        // Variables
        final Intent intent = new Intent(this, cls);

        // Empty activity memory
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent); // Open activity
    }

    /**
     * Takes appropriate action upon the success of the registration of a Card Collector.
     */
    public void registrationSuccessCollector() {
        openStartActivity(START_ACTIVITY_COLLECTOR); // Open the appropriate activity
        // Display collector-success message
        Toast.makeText(this, "Registered as Card Collector", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon the success of the registration of a Card Creator.
     */
    public void registrationSuccessCreator() {
        openStartActivity(START_ACTIVITY_CREATOR); // Open the appropriate activity
        // Display creator-success message
        Toast.makeText(this, "Registered as Card Creator", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon the database-failure of the registration.
     */
    public void registrationDatabaseFail() {
        // Display database-failure message
        Toast.makeText(this, "Registration has failed, please try again later", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon the confirmation-failure of the registration.
     */
    public void registrationConfirmationFail() {
        // Display confirmation-failure message
        Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon the password-length-failure of the registration.
     */
    public void registrationPasswordLengthFail() {
        // Display password-failure message
        Toast.makeText(this, "Password length must be at least " + PASSWORD_LENGTH + " characters", Toast.LENGTH_LONG).show();
    }

    /**
     * Takes appropriate action upon the email-string-failure of the registration.
     */
    public void registrationEmailStringFail() {
        // Display email-string-failure message
        Toast.makeText(this, "Fill in an email", Toast.LENGTH_LONG).show();
    }
}