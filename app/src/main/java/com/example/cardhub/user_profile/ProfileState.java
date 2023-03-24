package com.example.cardhub.user_profile;

import android.widget.ImageView;

public class ProfileState {
    Profile currentProfile;

    public ProfileState() {

    }

    public ImageView getProfilePicture() {
        return currentProfile.getProfilePicture();
    }

//    public getCardsCollectedAmount() {
//        return currentProfile.getCarsColl
//    }
}
