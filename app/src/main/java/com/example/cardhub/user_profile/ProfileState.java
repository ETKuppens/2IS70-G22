package com.example.cardhub.user_profile;

import android.widget.ImageView;

import com.example.cardhub.inventory.InventoryRepositoryImpl;

public class ProfileState implements ProfileRepositoryReceiver {
    private ProfileActivity activity;
    private CreatorProfileActivity creatorActivity;
    Profile currentProfile;

    //Repositories
    InventoryRepositoryImpl inventoryRepository;
    Profile profile;
    ProfileRepositoryImpl profileRepository;

    public ProfileState(ProfileActivity activity) {
        this.profileRepository = new ProfileRepositoryImpl(this);
        this.currentProfile = profileRepository.getProfile();
        this.activity = activity;
    }

    public ProfileState(CreatorProfileActivity activity) {
        this.profileRepository = new ProfileRepositoryImpl(this);
        this.currentProfile = profileRepository.getProfile();
        this.creatorActivity = activity;
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

    @Override
    public void receiverProfile(Profile profile) {
        activity.updateData();
    }
}
