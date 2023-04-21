package com.example.cardhub.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.cardhub.R;
import com.example.cardhub.TradingMode.CardDiff;
import com.example.cardhub.authentification.LoginActivity;
import com.example.cardhub.creator_navigation.CreatorBaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.Arrays;

public class CreatorInventoryActivity extends CreatorBaseActivity implements BaseInventoryActivity {

    private FirebaseAuth mAuth;
    private CardSorter.SortAttribute sortType = CardSorter.SortAttribute.RARITY;
    InventoryState state;
    CardGridAdapter adapter;

    boolean shouldSupportChoosingACard = false;
    CardDiff diff = null;
    Intent thisIntent;
    Intent displayCardIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_inventory);
        setupNav();
        this.thisIntent = getIntent();
        Bundle intentBundle = thisIntent.getExtras();

        if (intentBundle != null) {
            String intentOrigin = intentBundle.getString("origin");

            if (intentOrigin != null && intentOrigin.equals("TradeModeActivity")) {
                this.shouldSupportChoosingACard = true;
            }
        }

        state = new InventoryState(this);
        mAuth = FirebaseAuth.getInstance();

        state.requestUserCards();

        adapter = new CardGridAdapter(this, state.displayCards);
        CardGridView cardGridView = findViewById(R.id.creator_card_grid);
        cardGridView.setExpanded(true);
        cardGridView.setAdapter(adapter);

        Button sort = findViewById(R.id.creator_button_sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortingDialog(v);
            }
        });

//        Button show_collection = findViewById(R.id.creator_show_collection);
//        show_collection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                state.toggleCollection();
//            }
//        });
    }

    private void showSortingDialog(View v) {
        AlertDialog.Builder sortingDialog = new AlertDialog.Builder(this);
        String[] criteria = {"RARITY", "NAME"};
        int currentChoice = Arrays.asList(criteria).indexOf(sortType.toString());

        sortingDialog.setTitle("Select Sorting Criteria");

        sortingDialog.setSingleChoiceItems(criteria, currentChoice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Rarity
                        setSortType(CardSorter.SortAttribute.RARITY);
                        break;
                    case 1: // Name
                        setSortType(CardSorter.SortAttribute.NAME);
                        break;
                }
            }
        });

        sortingDialog.setPositiveButton("Sort", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                state.sortCards(sortType);
                updateGrid();
                Toast.makeText(CreatorInventoryActivity.this, "Sorting by " + sortType.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        sortingDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        sortingDialog.show();
    }

    private void setSortType(CardSorter.SortAttribute rarity) {
        sortType = rarity;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_inventory;
    }

    @Override
    public int getBottomNavigationMenuItemId() {
        return R.id.action_inventory;
    }

    /**
     * Stop this activity by using {@code finish()}.
     * If this InventoryActivity instance is launched with the purpose of choosing a card from the
     * inventory and a card has been chosen, set that card as a result.
     */
    public void stopActivity() {
        if (shouldSupportChoosingACard) {
            if (diff == null) {
                setResult(RESULT_CANCELED, thisIntent);
            } else { // diff != null
                Gson converter = new Gson();
                String convertedCardDiff = converter.toJson(diff);
                thisIntent.putExtra("CardDiff", convertedCardDiff);
                setResult(RESULT_OK, thisIntent);
            }
        }

        finish();
    }

    /**
     * Update the inventory grid
     */
    @Override
    public void updateGrid() {
        CardGridView cardGridView = findViewById(R.id.creator_card_grid);
        adapter.updateData(state.displayCards);
        adapter.notifyDataSetChanged();
        cardGridView.invalidateViews();

        Log.d("GRID_UPDATE", "cards length: " + state.displayCards.size());

        cardGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("FRAGMENT", "card clicked");
                displayCardIntent = new Intent(getApplicationContext(), CardActivity.class);

                Card cardToEncode = state.getCard(i);

                Gson converter = new Gson();
                String encodedCard = converter.toJson(cardToEncode);

                displayCardIntent.putExtra("card", encodedCard);
                if (!cardToEncode.acquired) {
                    displayCardIntent.putExtra("ShouldSupportChoosingACard", false);
                } else {
                    displayCardIntent.putExtra("ShouldSupportChoosingACard", shouldSupportChoosingACard);
                }

                cardPreviewResultLauncher.launch(displayCardIntent);
            }
        });
    }

    @Override
    public void updateCollectionButton() {
//        Button show_collection = findViewById(R.id.creator_show_collection);
//        if (state.showingInventory) {
//            show_collection.setText("Progress");
//        } else {
//            show_collection.setText("Show Inventory");
//        }
    }

    ActivityResultLauncher<Intent> cardPreviewResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() != RESULT_OK) {
                        return;
                    }
                    // result.getResultCode() == RESULT_OK

                    Gson converter = new Gson();
                    String encodedCard = displayCardIntent.getStringExtra("card");
                    Card card = converter.fromJson(encodedCard, Card.class);

                    diff = new CardDiff(card, CardDiff.DiffOption.ADD);

                    stopActivity();
                }
            }
    );

    @Override
    public void scrollBackToTop() {
        CardGridView cardGridView = findViewById(R.id.creator_card_grid);
        cardGridView.scrollTo(1, 1);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            this.startActivity(intent);
            startActivity(intent);
        }
    }
}
