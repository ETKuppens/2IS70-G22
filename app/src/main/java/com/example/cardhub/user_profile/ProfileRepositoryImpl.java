package com.example.cardhub.user_profile;


import com.example.cardhub.inventory.Card;

import java.util.ArrayList;
import java.util.Map;

public class ProfileRepositoryImpl implements ProfileRepository {
    ProfileData data;
    ProfileRepositoryReceiver receiver;

    ProfileRepositoryImpl(ProfileRepositoryReceiver receiver) {
        this.receiver = receiver;
        this.data = new ProfileData(this);
    }

    public void logOut() {
        data.logOut();
    }

    @Override
    public void receiverProfile(Map<String, Object> profile) {
        receiver.receiverProfile(new Profile(profile));
    }
}

