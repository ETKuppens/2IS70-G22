package com.example.cardhub.map;

import com.example.cardhub.inventory.Card;

import java.util.List;
import java.util.Map;

public interface MapRepository {
    /**
     * Requests all packs from the database
     */
    void requestPacks();

    /**
     * Receives all packs from the database
     *
     * @param packsRaw List of packs from the database
     */
    void receivePacks(List<Map<String, Object>> packsRaw);

    /**
     * Makes a request to the database to acquire a random card of given rarity.
     *
     * @param rarity Rarity of the card to acquire
     */
    void acquireRandomCard(Card.Rarity rarity);

    /**
     * Receives a random card from the database
     *
     * @param acquiredCard Map of the acquired card
     */
    void acquireRandomCardCallback(Map<String,Object> acquiredCard);
}
