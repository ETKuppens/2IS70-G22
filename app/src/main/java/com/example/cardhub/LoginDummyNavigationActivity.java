package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LoginDummyNavigationActivity extends AppCompatActivity {

    private Button loginButton;
    private RadioGroup roleRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the UI components
        loginButton = findViewById(R.id.login_button);
        roleRadioGroup = findViewById(R.id.role_radio_group);

        // Set the click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user's credentials and selected role from the input fields
                //String email = emailEditText.getText().toString();
                //String password = passwordEditText.getText().toString();
                int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();

                // If the credentials are valid, redirect the user to the appropriate activity
                if (selectedRoleId == R.id.collector_radio_button) {
                    Intent collectorIntent = new Intent(getApplicationContext(), Collector.CollectorMapActivity.class);
                    startActivity(collectorIntent);
                    //finish();
                } else if (selectedRoleId == R.id.creator_radio_button) {
                    Intent creatorIntent = new Intent(getApplicationContext(), Creator.CreatorInventoryActivity.class);
                    startActivity(creatorIntent);
                    //finish();
                } else {
                    // Show an error message if no role is selected
                    Toast.makeText(LoginDummyNavigationActivity.this, "Please select a role", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}