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

    }

    void updateData() {
        ImageView profilePic = findViewById(R.id.profilePicture);
        profilePic = state.getProfilePicture();

        TextView cardAmount = findViewById(R.id.cardsCollectedAmount);
        cardAmount.setText(state.);
    }
}