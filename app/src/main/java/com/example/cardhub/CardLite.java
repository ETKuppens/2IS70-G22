package com.example.cardhub;

import com.google.android.gms.maps.model.LatLng;

public class CardLite {
    public CardLite(LatLng position, String title, String description, int image) {
        this.position = position;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public LatLng position;
    public String title;
    public String description;
    public int image;


}
