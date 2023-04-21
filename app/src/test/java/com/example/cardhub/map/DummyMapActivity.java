package com.example.cardhub.map;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.cardhub.inventory.Card;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class DummyMapActivity extends MapActivity {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {}

    @Override
    protected void onSaveInstanceState(Bundle outState) {}

    @Override
    public void onMapReady(GoogleMap map) {}

    @Override
    public void addPointsOnTheMap() {}

    @Override
    public boolean onMarkerClick(final Marker marker) {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {}

    @Override
    public void onCompassClicked(View view) {}

    @Override
    protected void buttonCollectCardPackClicked(Card.Rarity rarity) {}


    private boolean cardsResponseWasCalled = false;
    private List<CardPack> packsGivenAtCardsResponse = null;

    @Override
    public void cardsResponse(List<CardPack> packs) {
        this.cardsResponseWasCalled = true;
        this.packsGivenAtCardsResponse = packs;
    }

    public boolean getCardsResponseWasCalled() {
        return this.cardsResponseWasCalled;
    }

    public List<CardPack> getPacksGivenAtCardsResponse() {
        return this.packsGivenAtCardsResponse;
    }


    private boolean showCardpackPreviewWindowWasCalled = false;
    private List<Card> cardPackCardsGivenAtShowCardpackPreviewWindow = null;

    @Override
    public void showCardpackPreviewWindow(List<Card> cardPackCards) {
        this.showCardpackPreviewWindowWasCalled = true;
        this.cardPackCardsGivenAtShowCardpackPreviewWindow = cardPackCards;
    }

    public boolean getShowCardpackPreviewWindowWasCalled() {
        return this.showCardpackPreviewWindowWasCalled;
    }

    public List<Card> getCardPackCardsGivenAtShowCardpackPreviewWindow() {
        return this.cardPackCardsGivenAtShowCardpackPreviewWindow;
    }

    @Override
    public void onStart() {}
}
