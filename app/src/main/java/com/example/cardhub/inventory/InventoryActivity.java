package com.example.cardhub.inventory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.cardhub.R;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {
    InventoryState state;
    CardGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        state = new InventoryState(this);

        state.requestUserCards();

        adapter = new CardGridAdapter(this, state.displayCards);
        GridView cardGridView = findViewById(R.id.card_grid);
        cardGridView.setAdapter(adapter);

        Button name_sort = findViewById(R.id.sort_by_name);
        name_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.sortCards(CardSorter.SortAttribute.NAME);
            }
        });

        Button rarity_sort = findViewById(R.id.sort_by_rarity);
        rarity_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.sortCards(CardSorter.SortAttribute.RARITY);
            }
        });

        Button show_collection = findViewById(R.id.show_collection);
        show_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.toggleCollection();
            }
        });
    }

    /**
     * Update the inventory grid
     */
    public void updateGrid() {
        GridView cardGridView = findViewById(R.id.card_grid);
        adapter.updateData(state.displayCards);
        adapter.notifyDataSetChanged();


        Log.d("GRID_UPDATE", "cards length: " + state.displayCards.size());

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

    public void updateCollectionButton() {
        Button show_collection = findViewById(R.id.show_collection);
        if (state.showingInventory) {
            show_collection.setText("Show Collection");
            ImageView image  = findViewById(R.id.card_image);
        } else {
            //List<Card> missingCards  = state.displayCards;
            //missingCards.stream().filter((Card card) -> !(state.userCards.stream().anyMatch((Card card2) -> card.NAME == card2.NAME)));
            //for (int i = 0; i < state.displayCards.size(); i++) {
            //    if (state.userCards.stream().anyMatch(card -> card.NAME == ))
            //    if
            //}
            //image.setColorFIlter(ContextCompat.getColor(this, R.color.black));
            show_collection.setText("Show Inventory");
        }
    }


}