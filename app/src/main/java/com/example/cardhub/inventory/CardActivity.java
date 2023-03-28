package com.example.cardhub.inventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cardhub.Card;
import com.example.cardhub.R;

import java.io.InputStream;
import java.net.URL;

public class CardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Card card = (Card) intent.getSerializableExtra("card");

        TextView nameView = findViewById(R.id.card_title);
        nameView.setText(card.NAME);

        TextView descView = findViewById(R.id.card_description);
        descView.setText(card.DESCRIPTION);

        ImageView imageView = findViewById(R.id.card_image);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("CARD_ACTIVITY", "jo mama");
                    InputStream is = (InputStream) new URL(card.IMAGE_URL).getContent();
                    Drawable d = Drawable.createFromStream(is, "src name");
                    imageView.setImageDrawable(d);
                } catch (Exception e) {
                    Log.d("CARD_ACTIVITY", e.toString());
                }
            }
        });
        thread.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }
}