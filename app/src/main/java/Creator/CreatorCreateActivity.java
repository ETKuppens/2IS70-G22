package Creator;

import com.myapplication.R;

public class CreatorCreateActivity extends CreatorBaseActivity {

    @Override
    int getLayoutId() {
        return R.layout.activity_creator_create;
    }

    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.action_create;
    }
}