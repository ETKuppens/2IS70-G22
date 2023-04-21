package com.example.cardhub.user_profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.cardhub.PairingModeActivity;
import com.example.cardhub.R;
import com.example.cardhub.authentication.LoginActivity;
import com.example.cardhub.collector_navigation.CollectorBaseActivity;
import com.example.cardhub.inventory.InventoryActivity;
import com.example.cardhub.map.MapActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends CollectorBaseActivity implements ProfileBaseActivity {
    private FirebaseAuth mAuth;
    ProfileState state;

    @Override
    public int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    public int getBottomNavigationMenuItemId() {
        return R.id.action_profile;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.state = new ProfileState(this);
        mAuth = FirebaseAuth.getInstance();

        state.requestProfile();

        setupNav();

        //Bind functionality to Buttons
        Button logoutButton = (Button)findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logout = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(logout);
                FirebaseAuth.getInstance().signOut();
                Log.d("USER", "LOGGED OUT!");
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_inventory) {
            startActivity(new Intent(this, InventoryActivity.class));
        } else if (itemId == R.id.action_map) {
            startActivity(new Intent(this, MapActivity.class));
        } else if (itemId == R.id.action_trading) {
            startActivity(new Intent(this, PairingModeActivity.class));
        } else if (itemId == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
        return true;
    }

    @Override
    public void updateData() {

        Log.d("PROFILE4", state.currentProfile.toString());

        TextView userName = findViewById(R.id.userName);
        userName.setText(state.currentProfile.getUsername());

        TextView cardAmount = findViewById(R.id.cardsCollectedAmount);
        cardAmount.setText(state.currentProfile.getCardAmount());

        TextView tradeAmount = findViewById(R.id.tradeAmount);
        tradeAmount.setText(state.currentProfile.getTradeAmount());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            this.startActivity(intent);
            startActivity(intent);
        }
    }
}