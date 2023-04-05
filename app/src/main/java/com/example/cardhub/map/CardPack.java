package com.example.cardhub.map;

import android.graphics.drawable.Drawable;

import com.example.cardhub.inventory.Card;
import com.google.android.gms.maps.model.LatLng;

public class CardPack {
    public LatLng position;
    public String name;
    public String description;
    public Card.Rarity rarity;
    public String image;
    public Drawable cachedImage;

    public CardPack(LatLng position, String name, String description, Card.Rarity rarity, String image) {
        this.position = position;
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.image = image;
    }
}
