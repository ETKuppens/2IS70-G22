package com.example.cardhub.map;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.cardhub.inventory.Card;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapRepositoryImpl implements MapRepository {
    MapState state;
    MapData data;
    MapRepositoryImpl(MapState state) {
        this.state = state;
        this.data = new MapData(this);
    }

    @Override
    public void requestPacks() {
        data.requestPacks();
    }

    @Override
    public void receivePacks(List<Map<String, Object>> packsRaw) {
        Log.d("BRUH", "receivePacks: " + packsRaw.get(0).get("image"));

        List<CardPack> packs = packsRaw.stream().map((Map<String, Object> pack) -> new CardPack(
                new LatLng(
                        ((GeoPoint) pack.get("position")).getLatitude(),
                        ((GeoPoint) pack.get("position")).getLongitude()
                ),
                (String) pack.get("name"),
                (String) pack.get("description"),
                Card.Rarity.values()[(int)(long)(pack.get("rarity"))],
                (String) pack.get("image"))).collect(Collectors.toList());

        state.setPacks(packs);
    }
}
