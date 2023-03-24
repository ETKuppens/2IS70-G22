package Creator;

import com.myapplication.R;

public class CreatorProfileActivity extends CreatorBaseActivity {

    @Override
    int getLayoutId() {
        return R.layout.activity_creator_profile;
    }

    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.action_profile;
    }
}