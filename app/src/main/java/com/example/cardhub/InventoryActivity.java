package com.example.cardhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class InventoryActivity extends AppCompatActivity {
    InventoryState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        state = new InventoryState();

        GridView cardGridView = findViewById(R.id.card_grid);
        cardGridView.setAdapter(new CardGridAdapter(getApplicationContext(), state.getCards()));

        cardGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(InventoryActivity.this, state.getCard(i).NAME, Toast.LENGTH_SHORT).show();
            }
        });
    }

}