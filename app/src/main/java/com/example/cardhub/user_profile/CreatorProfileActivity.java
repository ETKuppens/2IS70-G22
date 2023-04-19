package com.example.cardhub.user_profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.cardhub.PairingModeActivity;
import com.example.cardhub.R;
import com.example.cardhub.authentification.LoginActivity;
import com.example.cardhub.creator_navigation.CreatorBaseActivity;
import com.example.cardhub.inventory.InventoryActivity;
import com.example.cardhub.map.MapActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity that is responsible for showing the user it's profile.
 * @Author Tulgar and Rijkman
 */
public class CreatorProfileActivity extends CreatorBaseActivity implements ProfileBaseActivity {
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
        setContentView(R.layout.activity_creator_profile);

        this.state = new ProfileState(this);
        mAuth = FirebaseAuth.getInstance();

        state.requestProfile();

        setupNav();

        TextView cardAmount = findViewById(R.id.cardsCollectedAmount);
        TextView cardsCollected = findViewById(R.id.cardsCollected);
        cardAmount.setVisibility(View.INVISIBLE);
        cardsCollected.setVisibility(View.INVISIBLE);

        TextView tradeAmount = findViewById(R.id.tradeAmount);
        TextView trades = findViewById(R.id.trades);
        tradeAmount.setVisibility(View.INVISIBLE);
        trades.setVisibility(View.INVISIBLE);


        //Bind functionality to Logout Button
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

    @Override
    public void updateData() {
        TextView userName = findViewById(R.id.userName);
        userName.setText(state.getUsername());

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
