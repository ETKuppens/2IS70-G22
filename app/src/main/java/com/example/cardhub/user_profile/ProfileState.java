package com.example.cardhub.user_profile;

import android.graphics.Picture;
import android.widget.ImageView;

import com.example.cardhub.R;
import com.example.cardhub.inventory.InventoryRepository;
import com.example.cardhub.inventory.InventoryRepositoryImpl;

import java.util.Collections;

public class ProfileState {
    Profile currentProfile;

    //Repositories
    InventoryRepositoryImpl inventoryRepository;
    ProfileRepository profileRepository;
    public ProfileState() {
        this.currentProfile = new Profile(
                null,
                "Hugh Mongous",
                Collections.emptyList()
        );

    }

    public ImageView getProfilePicture() {
        return currentProfile.getProfilePicture();
    }

    public String getCardAmount() {
        return currentProfile.getCardAmount();
    }

    public String getUsername() {
        return currentProfile.getUsername();
    }

    public String getTradeAmount() {
        return "5";
    }
}
