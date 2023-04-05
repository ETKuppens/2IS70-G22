package com.example.cardhub.creator_navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cardhub.R;
import com.example.cardhub.card_creation.CardCreationActivity;
import com.example.cardhub.inventory.CreatorInventoryActivity;
import com.example.cardhub.user_profile.CreatorProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public abstract class CreatorBaseActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

    }

    protected void setupNav() {
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_inventory) {
                    startActivity(new Intent(getApplicationContext(), CreatorInventoryActivity.class));
                } else if (itemId == R.id.action_profile) {
                    startActivity(new Intent(getApplicationContext(), CreatorProfileActivity.class));
                } else if (itemId == R.id.action_create) {
                    startActivity(new Intent(getApplicationContext(), CardCreationActivity.class));
                }
                return true;
            }
        });
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

    abstract public int getLayoutId();

    abstract public int getBottomNavigationMenuItemId();//Which menu item selected and change the state of that menu item
}
