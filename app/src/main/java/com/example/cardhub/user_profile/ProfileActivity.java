package com.example.cardhub.user_profile;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.cardhub.MapActivity;
import com.example.cardhub.PairingModeActivity;
import com.example.cardhub.collector_navigation.CollectorBaseActivity;
import com.example.cardhub.authentification.LoginActivity;
import com.example.cardhub.R;
import com.example.cardhub.inventory.InventoryActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends CollectorBaseActivity {
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

        this.state = new ProfileState();
        updateData();

        setupNav();

        //Bind functionality to Buttons
        Button logoutButton = (Button)findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logout);
                FirebaseAuth.getInstance().signOut();
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

    void updateData() {
        ImageView profilePic = findViewById(R.id.profilePicture);
        profilePic = state.getProfilePicture();

        TextView userName = findViewById(R.id.userName);
        userName.setText(state.getUsername());

        TextView cardAmount = findViewById(R.id.cardsCollectedAmount);
        cardAmount.setText(state.getCardAmount());

        TextView tradeAmount = findViewById(R.id.tradeAmount);
        tradeAmount.setText(state.getTradeAmount());
    }


}