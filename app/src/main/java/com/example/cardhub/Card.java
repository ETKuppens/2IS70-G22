package com.example.cardhub;

/**
 * Class that stores data of a card instance.
 */
public class Card {
    /**
     * Enum for the rarity of a card.
     */
    enum Rarity {
        COMMON,
        RARE,
        LEGENDARY,
        UNKNOWN
    };

    final String NAME;        // The name of this card
    final String DESCRIPTION; // An optional description of the card
    final Rarity RARITY;      // The rarity of this card
    final String IMAGE_URL;       // The image of this card

    /**
     * Instantiate a card with a description
     */
    Card(String name, String description, Rarity rarity, String imageUrl) {
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
}