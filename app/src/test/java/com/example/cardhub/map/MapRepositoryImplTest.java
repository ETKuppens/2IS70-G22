package com.example.cardhub.map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.example.cardhub.inventory.Card;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.GeoPoint;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapRepositoryImplTest extends TestCase {
    @Mock
    MapData data;
    @Mock
    MapState state;

    MapRepositoryImpl repo;

    @Captor
    ArgumentCaptor<List<CardPack>> packCaptor;

    public MapRepositoryImplTest() {
        MockitoAnnotations.openMocks(this);
        repo = new MapRepositoryImpl(state, data);
    }

    public void testRequestPacks() {
        repo.requestPacks();
        Mockito.verify(data).requestPacks();
    }

    public void testReceivePacks() {
        CardPack pack = new CardPack(new LatLng(0,0), "test", "test", Card.Rarity.LEGENDARY, "test");

        GeoPoint position = new GeoPoint(0,0);

        Map<String, Object>serializedPack = new HashMap<>();
        serializedPack.put("position", position);
        serializedPack.put("name", pack.name);
        serializedPack.put("description", pack.description);
        serializedPack.put("rarity", pack.rarity.toString());
        serializedPack.put("image", pack.image);

        repo.receivePacks(Collections.singletonList(serializedPack));

        Mockito.verify(state).setPacks(Collections.singletonList(pack));
    }

    public void testAcquireRandomCard() {
        repo.acquireRandomCard(Card.Rarity.LEGENDARY);

        Mockito.verify(data).acquireRandomCard(Card.Rarity.LEGENDARY);
    }

    public void testAcquireRandomCardCallback() {
        Card card = new Card( "test", "test", Card.Rarity.LEGENDARY, "test");

        Map<String, Object>serializedPack = card.serialize();

        repo.acquireRandomCardCallback(serializedPack);

        Mockito.verify(state).acquireRandomCardCallback(card);
    }
}