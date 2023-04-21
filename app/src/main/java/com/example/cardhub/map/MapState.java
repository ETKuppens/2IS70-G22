package com.example.cardhub.map;

import com.example.cardhub.inventory.Card;

import java.util.Arrays;
import java.util.List;

public class MapState implements MapRepositoryReceiver {
    MapActivity activity;
    MapRepository repository;

    List<CardPack> packs;

    public MapState(MapActivity activity) {
        this.activity = activity;
        this.repository = new MapRepositoryImpl(this);
    }

    public MapState(MapActivity activity, MapRepository repository) {
        this.activity = activity;
        this.repository = repository;
    }

    public void requestPacks() {
        repository.requestPacks();
    }

    @Override
    public void setPacks(List<CardPack> packs) {
        this.packs = packs;
        activity.cardsResponse(packs);
    }

    public void acquireRandomCard(Card.Rarity rarity) {
        repository.acquireRandomCard(rarity);
    }

    public void acquireRandomCardCallback(Card acquiredCard) {
        activity.showCardpackPreviewWindow(Arrays.asList(acquiredCard));
    }
}
