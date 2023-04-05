package com.example.cardhub.user_profile;


import com.example.cardhub.inventory.Card;

import java.util.ArrayList;

public class ProfileRepository {
    ProfileData data;
    ProfileState state;

    ProfileRepository(ProfileState state) {
        this.state = state;
    }

    public Profile getProfile() {
        //return data.getProfile();
        ArrayList<Card> inv = new ArrayList<Card>();
        inv.add(new Card("Gabagonga", "ges", Card.Rarity.COMMON, ""));
        inv.add(new Card("Gabagonga", "ges", Card.Rarity.COMMON, ""));
        inv.add(new Card("Gabagonga", "ges", Card.Rarity.COMMON, ""));
        inv.add(new Card("Gabagonga", "ges", Card.Rarity.COMMON, ""));
        inv.add(new Card("Gabagonga", "ges", Card.Rarity.COMMON, ""));
        return new Profile(null, "Gabriel Louis", inv);
    }

    public void logOut() {
        data.logOut();
    }
}

