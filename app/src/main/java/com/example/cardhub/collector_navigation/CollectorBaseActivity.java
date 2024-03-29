package com.example.cardhub.collector_navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cardhub.PairingMode.PairingModeActivity;
import com.example.cardhub.R;
import com.example.cardhub.inventory.InventoryActivity;
import com.example.cardhub.map.MapActivity;
import com.example.cardhub.user_profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public abstract class CollectorBaseActivity extends AppCompatActivity
        implements NavigationBarView.OnItemSelectedListener {

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
                navigationView.postDelayed(() -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.action_inventory) {
                        startActivity(new Intent(getApplicationContext(), InventoryActivity.class));
                    } else if (itemId == R.id.action_map) {
                        startActivity(new Intent(getApplicationContext(), MapActivity.class));
                    } else if (itemId == R.id.action_trading) {
                        startActivity(new Intent(getApplicationContext(),
                                PairingModeActivity.class));
                    } else if (itemId == R.id.action_profile) {
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    }
                    finish();
                }, 300);
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

    // this is to return which layout(activity) needs to display when clicked on tabs.
    abstract public int getLayoutId();

    //Which menu item selected and change the state of that menu item
    abstract public int getBottomNavigationMenuItemId();
}
