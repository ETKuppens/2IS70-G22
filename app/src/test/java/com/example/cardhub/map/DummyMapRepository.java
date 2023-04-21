package com.example.cardhub.map;

import com.example.cardhub.inventory.Card;

import java.util.List;
import java.util.Map;

public class DummyMapRepository implements MapRepository {
    private boolean requestPacksWasCalled = false;

    @Override
    public void requestPacks() {
        this.requestPacksWasCalled = true;
    }

    public boolean getRequestPacksWasCalled() {
        return this.requestPacksWasCalled;
    }


    private boolean receivePacksWasCalled = false;
    private List<Map<String, Object>> packsRawGivenAtReceivePacks = null;

    @Override
    public void receivePacks(List<Map<String, Object>> packsRaw) {
        this.receivePacksWasCalled = true;
        this.packsRawGivenAtReceivePacks = packsRaw;
    }

    public boolean getReceivePacksWasCalled() {
        return this.receivePacksWasCalled;
    }

    public List<Map<String, Object>> getPacksRawGivenAtReceivePacks() {
        return this.packsRawGivenAtReceivePacks;
    }


    private boolean acquireRandomCardWasCalled = false;
    private Card.Rarity rarityGivenAtAcquireRandomCard = null;

    @Override
    public void acquireRandomCard(Card.Rarity rarity) {
        this.acquireRandomCardWasCalled = true;
        this.rarityGivenAtAcquireRandomCard = rarity;
    }

    public boolean getAcquireRandomCardWasCalled() {
        return this.acquireRandomCardWasCalled;
    }

    public Card.Rarity getRarityGivenAtAcquireRandomCard() {
        return this.rarityGivenAtAcquireRandomCard;
    }


    private boolean acquireRandomCardCallbackWasCalled = false;
    private Map<String, Object> acquiredCardGivenAtAcquireRandomCardCallback = null;

    @Override
    public void acquireRandomCardCallback(Map<String,Object> acquiredCard) {
        this.acquireRandomCardCallbackWasCalled = true;
        this.acquiredCardGivenAtAcquireRandomCardCallback = acquiredCard;
    }

    public boolean getAcquireRandomCardCallbackWasCalled() {
        return this.acquireRandomCardCallbackWasCalled;
    }

    public Map<String, Object> getAcquiredCardGivenAtAcquireRandomCardCallback() {
        return this.acquiredCardGivenAtAcquireRandomCardCallback;
    }
}
