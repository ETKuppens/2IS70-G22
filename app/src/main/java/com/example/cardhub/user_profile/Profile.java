package com.example.cardhub.user_profile;

import android.widget.ImageView;

import com.example.cardhub.inventory.Card;

import java.util.List;
import java.util.Map;

public class Profile {
    private String userName;

    private ImageView profilePicture;

    private int cardsCollected;
    private int tradesMade;

    public Profile(ImageView profilePicture, String userName, int cardsCollected, int tradesMade) {
        this.profilePicture = profilePicture;
        this.userName = userName;
        this.cardsCollected = cardsCollected;
        this.tradesMade = tradesMade;
    }

    public Profile(Map<String, Object>data) {
        //this.profilePicture = (String) profilePicture;
        this.userName = (String) data.get("username");
        this.cardsCollected = (int) data.get("cardscollected");
        this.tradesMade = (int) data.get("tradesmade");
    }

    public void setProfilePicture(ImageView profilePicture) {
        this.profilePicture = profilePicture;
    }

    public ImageView getProfilePicture() {
        return profilePicture;
    }

    public String getCardAmount() {
        return String.valueOf(this.cardsCollected);
    }

    public String getTradeAmount() {
        return String.valueOf(this.tradesMade);
    }


    public String getUsername() {
        return this.userName;
    }

}
