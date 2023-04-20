package com.example.cardhub.user_profile;

import android.util.Log;
import android.widget.ImageView;

import com.example.cardhub.inventory.Card;

import java.util.List;
import java.util.Map;

/**
 * Data structure that represents a user profile
 * @author Rijkman and Tulgar
 */
public class Profile {
    //Stores the profiles Username
    private String userName;
    //Users profile picture
    private ImageView profilePicture;
    //Amount of cards collected
    private int cardsCollected;
    //Amount of trades made
    private int tradesMade;

    public Profile(ImageView profilePicture, String userName, int cardsCollected, int tradesMade) {
        this.profilePicture = profilePicture;
        this.userName = userName;
        this.cardsCollected = cardsCollected;
        this.tradesMade = tradesMade;
    }

    public Profile(Map<String, Object>data) {
        //this.profilePicture = (String) profilePicture;
        Log.d("PROFILE2", data.toString());
        this.userName = (String) data.get("email");
        this.cardsCollected = (int)  data.get("cardamount");
        this.tradesMade = (int)  data.get("tradesmade");
    }

    /**
     * Changes the current profile picture to a new one.
     * @param profilePicture the profile picture to replace it with
     * @throws NullPointerException if profilePicture == null
     */
    public void setProfilePicture(ImageView profilePicture) {
        if (profilePicture == null) {
            throw new NullPointerException();
        }
        this.profilePicture = profilePicture;
    }

    /**
     * Returns the current profile picture.
     * @return the picture as an ImageView object
     */
    public ImageView getProfilePicture() {
        return profilePicture;
    }

    /**
     * Returns the amount of cards this user has.
     * @return the amount of cards as a string object
     */
    public String getCardAmount() {
        return String.valueOf(this.cardsCollected);
    }

    /**
     * Returns the amount of trades made by the current user.
     * @return the amount of trades a String object
     */
    public String getTradeAmount() {
        return String.valueOf(this.tradesMade);
    }

    /**
     * Returns the username of the current user.
     * @return the username as string object
     */
    public String getUsername() {
        Log.d("PROFILE3", userName);
        return String.valueOf(this.userName);
    }

}
