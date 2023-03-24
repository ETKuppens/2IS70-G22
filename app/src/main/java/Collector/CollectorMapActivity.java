package Collector;

import com.example.cardhub.R;

public class CollectorMapActivity extends CollectorBaseActivity {

    @Override
    int getLayoutId() {
        return R.layout.activity_collector_map;
    }

    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.action_map;
    }
}