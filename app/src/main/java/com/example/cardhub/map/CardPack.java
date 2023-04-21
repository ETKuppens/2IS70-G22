package com.example.cardhub.map;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.example.cardhub.inventory.Card;
import com.google.android.gms.maps.model.LatLng;

public class CardPack {
    public LatLng position;
    public String name;
    public String description;
    public Card.Rarity rarity;
    public String image;

    public CardPack(LatLng position, String name, String description, Card.Rarity rarity, String image) {
        this.position = position;
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.image = image;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof CardPack)) {return false;}
        else {
            CardPack other = (CardPack) obj;
            return other.position.equals(this.position) &&
                    other.name.equals(this.name) &&
                    other.description.equals(this.description) &&
                    other.rarity.equals(this.rarity) &&
                    other.image.equals(this.image);
        }
    }
}
