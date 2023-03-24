package Collector;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.cardhub.R;

public abstract class CollectorBaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

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
                startActivity(new Intent(this, CollectorInventoryActivity.class));
            } else if (itemId == R.id.action_map) {
                startActivity(new Intent(this, CollectorMapActivity.class));
            } else if (itemId == R.id.action_trading) {
                startActivity(new Intent(this, CollectorTradingActivity.class));
            } else if (itemId == R.id.action_profile) {
                startActivity(new Intent(this, CollectorProfileActivity.class));
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

    abstract int getLayoutId(); // this is to return which layout(activity) needs to display when clicked on tabs.

    abstract int getBottomNavigationMenuItemId();//Which menu item selected and change the state of that menu item
}
