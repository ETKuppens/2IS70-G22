package com.example.cardhub.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.cardhub.CardDiff;
import com.example.cardhub.R;

import com.google.gson.Gson;

public class InventoryActivity extends AppCompatActivity {
    InventoryState state;

    boolean shouldSupportChoosingACard = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();

        if (intentBundle != null) {
            String intentOrigin = intentBundle.getString("origin");

            if (intentOrigin != null && intentOrigin.equals("TradeModeActivity")) {
                this.shouldSupportChoosingACard = true;
            }
        }

        CardDiff diff = null;
        Gson converter = new Gson();
        String convertedCardDiff = converter.toJson(diff);
        intent.putExtra("CardDiff", convertedCardDiff);
        setResult(RESULT_OK, intent);
        finish();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        state = new InventoryState(this);

        state.requestCards();
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
                Intent intent = new Intent(getApplicationContext(), CardActivity.class);
                intent.putExtra("card", state.getCard(i));
                startActivity(intent);
            }
        });
    }


}