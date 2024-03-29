package com.example.cardhub.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cardhub.R;
import com.example.cardhub.authentication.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class CardActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean shouldSupportChoosingACard = false;
    private FirebaseAuth mAuth;

    private Intent intent; // Intent of this CardActivity

    private Button cancelButton;
    private Button confirmButton;

    /**
     * This method sets up the Card activity by initializing various UI elements,
     * checking if the activity should support choosing a card, and loading the details of the card.
     * @param savedInstanceState the saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        this.cancelButton = findViewById(R.id.button_card_activity_cancel);
        this.confirmButton = findViewById(R.id.button_card_activity_confirm);

        intent = getIntent();
        mAuth = FirebaseAuth.getInstance();

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

    /**
     * This method handles the selection of an item in the options menu.
     * If the selected item is the home button, the activity is finished.
     * @param item the selected menu item
     * @return true if the item selection is handled, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

    /**
     * This method handles click events for the cancel and confirm buttons in the Card activity.
     * If the activity should not support choosing a card, the method does nothing.
     * Otherwise, it sets the result of the activity based on which button was clicked and
     * finishes the activity.
     * @param view the view that was clicked
     */
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

    /**
     * This method is called when the activity is becoming visible to the user.
     * It checks if the user is signed in and updates the UI accordingly.
     * If the user is not signed in, the method launches the LoginActivity and clears
     * the task stack.
     */
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