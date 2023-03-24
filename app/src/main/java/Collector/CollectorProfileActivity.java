package Collector;

import com.myapplication.R;

public class CollectorProfileActivity extends CollectorBaseActivity {

    @Override
    int getLayoutId() {
        return R.layout.activity_collector_profile;
    }

    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.action_profile;
    }
}