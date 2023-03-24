package Collector;

import com.myapplication.R;

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