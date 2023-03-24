package Creator;

import com.example.cardhub.R;

public class CreatorInventoryActivity extends CreatorBaseActivity {

    @Override
    int getLayoutId() {
        return R.layout.activity_creator_inventory;
    }

    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.action_inventory;
    }
}