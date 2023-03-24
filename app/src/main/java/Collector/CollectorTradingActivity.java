package Collector;

import com.myapplication.R;

public class CollectorTradingActivity extends CollectorBaseActivity {

    @Override
    int getLayoutId() {
        return R.layout.activity_collector_trading;
    }

    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.action_trading;
    }
}