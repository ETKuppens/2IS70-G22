package Collector;

import com.example.cardhub.R;

public class CollectorInventoryActivity extends CollectorBaseActivity {

    @Override
    int getLayoutId() {
        return R.layout.activity_collector_inventory;
    }

    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.action_inventory;
    }
}