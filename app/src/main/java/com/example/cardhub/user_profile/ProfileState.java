package com.example.cardhub.user_profile;

import android.widget.ImageView;

import com.example.cardhub.inventory.InventoryRepositoryImpl;

public class ProfileState {
    Profile currentProfile;

    //Repositories
    InventoryRepositoryImpl inventoryRepository;
    ProfileRepository profileRepository;
    public ProfileState() {
        this.profileRepository = new ProfileRepository(this);
        this.currentProfile = profileRepository.getProfile();
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

    public void logout() {
        currentProfile = null;
    }
}
