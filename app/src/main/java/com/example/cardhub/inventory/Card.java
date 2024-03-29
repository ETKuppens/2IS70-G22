package com.example.cardhub.inventory;

import androidx.annotation.Nullable;

import com.example.cardhub.map.CardPack;

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

    public boolean acquired = true;

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
    public Card(String name, Rarity rarity, String imageUrl) {
        this.NAME = name;
        this.DESCRIPTION = "";
        this.RARITY = rarity;
        this.IMAGE_URL = imageUrl;
    }

    /**
     * Serialize the card.
     *
     * @return serialized data.
     */
    public Map<String, Object> serialize () {
        Map<String, Object> data = new HashMap<>();

        data.put("name", this.NAME);
        data.put("description", this.DESCRIPTION);
        data.put("rarity", this.RARITY.toString());
        data.put("imageurl", this.IMAGE_URL);

        return data;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Card)) {return false;}
        else {
            Card other = (Card) obj;
            return other.NAME.equals(this.NAME) &&
                    other.DESCRIPTION.equals(this.DESCRIPTION) &&
                    other.RARITY.equals(this.RARITY) &&
                    other.IMAGE_URL.equals(this.IMAGE_URL);
        }
    }
}