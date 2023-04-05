package com.example.cardhub.inventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cardhub.R;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;

public class CardActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean shouldSupportChoosingACard = false;

    private Intent intent; // Intent of this CardActivity

    private Button cancelButton;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.cancelButton = findViewById(R.id.button_card_activity_cancel);
        this.confirmButton = findViewById(R.id.button_card_activity_confirm);

        intent = getIntent();

        if (intent.getBooleanExtra("ShouldSupportChoosingACard", false)) {
            shouldSupportChoosingACard = true;

            this.cancelButton.setOnClickListener(this);
            this.confirmButton.setOnClickListener(this);

            this.cancelButton.setEnabled(true);
            this.confirmButton.setEnabled(true);

            this.cancelButton.setVisibility(View.VISIBLE);
            this.confirmButton.setVisibility(View.VISIBLE);
        } else {
            this.cancelButton.setEnabled(false);
            this.confirmButton.setEnabled(false);

            this.cancelButton.setVisibility(View.GONE);
            this.confirmButton.setVisibility(View.GONE);
        }

        Gson converter = new Gson();
        String encodedCard = intent.getStringExtra("card");
        Card card = converter.fromJson(encodedCard, Card.class);

        TextView nameView = findViewById(R.id.card_title);
        nameView.setText(card.NAME);

        TextView descView = findViewById(R.id.card_description);
        descView.setText(card.DESCRIPTION);

        ImageView imageView = findViewById(R.id.card_image);
        Glide.with(getApplicationContext()).load(card.IMAGE_URL).into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if (!this.shouldSupportChoosingACard) {
            return;
        }

        // Change functionality depending on which button was clicked.
        switch(view.getId()) {
            case R.id.button_card_activity_cancel:
                setResult(RESULT_CANCELED, intent);
                finish();
                break;

            case R.id.button_card_activity_confirm:
                setResult(RESULT_OK, intent);
                finish();
                break;

            default:
                break;
        }
    }
}