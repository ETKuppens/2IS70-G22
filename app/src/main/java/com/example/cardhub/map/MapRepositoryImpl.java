package com.example.cardhub.map;

import android.util.Log;

import com.example.cardhub.inventory.Card;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapRepositoryImpl implements MapRepository {
    MapState state;
    MapData data;

    MapRepositoryImpl(MapState state) {
        this.state = state;
        this.data = new MapData(this,
                FirebaseFirestore.getInstance(), FirebaseAuth.getInstance());
    }

    MapRepositoryImpl(MapState state, MapData data) {
        this.state = state;
        this.data = data;
    }

    @Override
    public void requestPacks() {
        data.requestPacks();
    }

    @Override
    public void receivePacks(List<Map<String, Object>> packsRaw) {
        List<CardPack> packs = packsRaw.stream().map((Map<String, Object> pack) -> new CardPack(
                new LatLng(
                        ((GeoPoint) pack.get("position")).getLatitude(),
                        ((GeoPoint) pack.get("position")).getLongitude()
                ),
                (String) pack.get("name"),
                (String) pack.get("description"),
                Card.Rarity.valueOf((String) (pack.get("rarity"))),
                (String) pack.get("image"))).collect(Collectors.toList());

        state.setPacks(packs);
    }

    @Override
    public void acquireRandomCard(Card.Rarity rarity) {
        data.acquireRandomCard(rarity);
    }

    /**
     * Receives a random card from the database
     *
     * @param acquiredCard Map of the acquired card
     */
    public void acquireRandomCardCallback(Map<String,Object> acquiredCard) {
        Card decodedCard = new Card(
                (String) acquiredCard.get("name"),
                (String) acquiredCard.get("description"),
                Card.Rarity.valueOf((String) acquiredCard.get("rarity")),
                (String) acquiredCard.get("imageurl"));

        state.acquireRandomCardCallback(decodedCard);
    }
}
