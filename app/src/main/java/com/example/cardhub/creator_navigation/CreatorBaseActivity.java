package com.example.cardhub.creator_navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cardhub.R;
import com.example.cardhub.card_creation.CardCreationActivity;
import com.example.cardhub.inventory.InventoryActivity;
import com.example.cardhub.user_profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class CreatorBaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_inventory) {
                startActivity(new Intent(this, InventoryActivity.class));
            } else if (itemId == R.id.action_create) {
                startActivity(new Intent(this, CardCreationActivity.class));
            } else if (itemId == R.id.action_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            }
            finish();
        }, 300);
        return true;
    }

    private void updateNavigationBarState() {
        int actionId = getBottomNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    abstract int getLayoutId();

    abstract int getBottomNavigationMenuItemId();//Which menu item selected and change the state of that menu item
}
