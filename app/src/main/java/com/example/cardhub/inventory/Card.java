package com.example.cardhub.inventory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that stores data of a card instance.
 */
public class Card implements Serializable {
    /**
     * Enum for the rarity of a card.
     */
    public enum Rarity {
        COMMON,
        RARE,
        LEGENDARY,
        ULTRA_RARE
    }

    public final String NAME;        // The name of this card
    public final String DESCRIPTION; // An optional description of the card
    public final Rarity RARITY;      // The rarity of this card
    public final String IMAGE_URL;       // The image of this card

    /**
     * Instantiate a card with a description
     */
    public Card(String name, String description, Rarity rarity, String imageUrl) {
        this.NAME = name;
        this.DESCRIPTION = description;
        this.RARITY = rarity;
        this.IMAGE_URL = imageUrl;
    }

    /**
     * Instantiate a card without a description
     */
    Card(String name, Rarity rarity, String imageUrl) {
        this.NAME = name;
        this.DESCRIPTION = "";
        this.RARITY = rarity;
        this.IMAGE_URL = imageUrl;
    }

    public Map<String, Object> serialize () {
        Map<String, Object> data = new HashMap<>();

        data.put("name", this.NAME);
        data.put("description", this.DESCRIPTION);
        data.put("rarity", this.RARITY);
        data.put("imageurl", this.IMAGE_URL);

        return data;
    }
}