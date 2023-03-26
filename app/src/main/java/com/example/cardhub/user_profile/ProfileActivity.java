package com.example.cardhub.user_profile;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cardhub.R;

public class ProfileActivity extends AppCompatActivity {
    ProfileState state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.state = new ProfileState();
        updateData();
    }

    void updateData() {
        ImageView profilePic = findViewById(R.id.profilePicture);
        profilePic = state.getProfilePicture();

        TextView userName = findViewById(R.id.userName);
        userName.setText(state.getUsername());

        TextView cardAmount = findViewById(R.id.cardsCollectedAmount);
        cardAmount.setText(state.getCardAmount());

        TextView tradeAmount = findViewById(R.id.tradeAmount);
        tradeAmount.setText(state.getTradeAmount());
    }
}