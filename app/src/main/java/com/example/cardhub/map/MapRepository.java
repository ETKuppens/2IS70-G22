package com.example.cardhub.map;

import com.example.cardhub.inventory.Card;

import java.util.List;
import java.util.Map;

public interface MapRepository {
    void requestPacks();

    void receivePacks(List<Map<String, Object>> packsRaw);

    void acquireRandomCard(Card.Rarity rarity);

    void acquireRandomCardCallback(Map<String,Object> acquiredCard);
}
