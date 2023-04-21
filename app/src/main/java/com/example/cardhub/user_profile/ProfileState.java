package com.example.cardhub.user_profile;

import android.widget.ImageView;

import com.example.cardhub.inventory.InventoryRepositoryImpl;


/**
 * Class responsible for managing the state of the Profile Activity
 * @author Rijkman and Tulgar
 */
public class ProfileState implements ProfileRepositoryReceiver {
    //Activity to callback to
    private ProfileBaseActivity activity;
    //Instance of the current Profile
    Profile currentProfile;

    //Repository
    ProfileRepositoryImpl profileRepository;

    public ProfileState(ProfileBaseActivity activity) {
        this.profileRepository = new ProfileRepositoryImpl(this);
        this.activity = activity;
    }

    /**
     * Returns the profile picture of the currently selected class.
     * @return an ImageView object of the picture
     */
    public ImageView getProfilePicture() {
        return currentProfile.getProfilePicture();
    }

    /**
     * Get the the amount of cards owned by the current profile.
     * @return the amount represented as a string.
     */
    public String getCardAmount() {
        return currentProfile.getCardAmount();
    }

    /**
     * Returns the username of the current profile.
     * @return the username as a string
     */
    public String getUsername() {
        return currentProfile.getUsername();
    }

    /**
     * Returns the amount of trades made by the current profile
     * @return the amount of trades as a string
     */
    public String getTradeAmount() {
        return currentProfile.getTradeAmount();
    }

    /**
     * Logs the out the current user profile.
     */
    public void logout() {
        currentProfile = null;
    }

    @Override
    public void receiverProfile(Profile profile) {
        this.currentProfile = profile;
        activity.updateData();
    }

    public void requestProfile() {
        profileRepository.requestProfile();
    }
}
