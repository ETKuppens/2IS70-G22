package com.example.cardhub.inventory;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.cardhub.Card;
import com.example.cardhub.TradingMode.CardDiff;
import com.example.cardhub.R;

import com.example.cardhub.collector_navigation.CollectorBaseActivity;
import com.google.gson.Gson;

public class InventoryActivity extends CollectorBaseActivity {
    InventoryState state;

    boolean shouldSupportChoosingACard = false;
    CardDiff diff = null;
    Intent thisIntent;
    Intent displayCardIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        this.thisIntent = getIntent();
        Bundle intentBundle = thisIntent.getExtras();

        if (intentBundle != null) {
            String intentOrigin = intentBundle.getString("origin");

            if (intentOrigin != null && intentOrigin.equals("TradeModeActivity")) {
                this.shouldSupportChoosingACard = true;
            }
        }

        state = new InventoryState(this);

        state.requestCards();
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
    public void updateGrid() {
        GridView cardGridView = findViewById(R.id.card_grid);
        cardGridView.setAdapter(new CardGridAdapter(getApplicationContext(), state.cards));
        cardGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("FRAGMENT", "card clicked");
                displayCardIntent = new Intent(getApplicationContext(), CardActivity.class);

                Card cardToEncode = state.getCard(i);

                Gson converter = new Gson();
                String encodedCard = converter.toJson(cardToEncode);

                displayCardIntent.putExtra("card", encodedCard);
                displayCardIntent.putExtra("ShouldSupportChoosingACard", shouldSupportChoosingACard);

                cardPreviewResultLauncher.launch(displayCardIntent);
            }
        });
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

}