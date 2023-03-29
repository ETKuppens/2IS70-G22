package com.example.cardhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cardhub.collector_navigation.CollectorBaseActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MapActivity extends CollectorBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Instantiate layout components
        Button btn_logout = findViewById(R.id.button_logout);
        Button btn_pairing = findViewById(R.id.button_pairing);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log user out
                FirebaseAuth.getInstance().signOut();

                // Show toast
                Toast.makeText(MapActivity.this, "Logged out!",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                // Ensure no returns to login screen
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btn_pairing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PairingModeActivity.class));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    public int getBottomNavigationMenuItemId() {
        return R.id.action_map;
    }
}