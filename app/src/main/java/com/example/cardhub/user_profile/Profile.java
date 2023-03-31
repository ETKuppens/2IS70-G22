package com.example.cardhub.user_profile;

import android.graphics.Picture;
import android.widget.ImageView;

import com.example.cardhub.inventory.Card;

import java.util.List;

public class Profile {
    private String userName;

    private ImageView profilePicture;

    private List<Card> inventory;

    public Profile(ImageView profilePicture, String userName, List<Card> inventory) {
        this.profilePicture = profilePicture;
        this.userName = userName;
        this.inventory = inventory;
    }

    public void setProfilePicture(ImageView profilePicture) {
        this.profilePicture = profilePicture;
    }

    public ImageView getProfilePicture() {
        return profilePicture;
    }

    public String getCardAmount() {
        return String.valueOf(inventory.size());
    }

    public String getTradeAmount() {
        return "0";
        // TODO: Implement trade amount;
    }

    public void setInventory(List<Card> cards) {
        this.inventory = cards;
    }

    public String getUsername() {
        return this.userName;
    }
}
