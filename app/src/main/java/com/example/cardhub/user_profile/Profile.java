package com.example.cardhub.user_profile;

import android.graphics.Picture;
import android.widget.ImageView;

public class Profile {
    private String userName;
    private int cardAmount;
    private int tradeAmount;

    public ImageView getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ImageView profilePicture) {
        this.profilePicture = profilePicture;
    }

    private ImageView profilePicture;

    public Profile(ImageView profilePicture, String userName, int cardAmount, int tradeAmount) {
        this.profilePicture = profilePicture;
        this.userName = userName;
        this.cardAmount = cardAmount;
        this.tradeAmount = tradeAmount;
    }
}
