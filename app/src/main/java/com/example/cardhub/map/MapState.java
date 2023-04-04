package com.example.cardhub.map;

import java.util.List;
import java.util.Map;

public class MapState implements MapRepositoryReceiver {
    MapActivity activity;
    MapRepository repository;

    List<CardPack> packs;

    public MapState(MapActivity activity) {
        this.activity = activity;
        this.repository = new MapRepositoryImpl(this);
    }

    public void requestPacks() {
        repository.requestPacks();
    }

    @Override
    public void setPacks(List<CardPack> packs) {
        this.packs = packs;
        activity.cardsResponse(packs);
    }
}
