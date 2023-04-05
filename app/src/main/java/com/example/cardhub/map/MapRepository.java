package com.example.cardhub.map;

import com.example.cardhub.inventory.Card;

import java.util.List;
import java.util.Map;

public interface MapRepository {
    public void requestPacks();

    void receivePacks(List<Map<String, Object>> packsRaw);

    public void acquireRandomCard(Card.Rarity rarity);

    void acquireRandomCardCallback(Card acquiredCard);
}
