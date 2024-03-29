package com.example.cardhub.inventory;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InventoryRepositoryImpl implements InventoryRepository {
    InventoryData data;
    InventoryRepositoryReceiver receiver;

    public InventoryRepositoryImpl(InventoryRepositoryReceiver receiver) {
        data = new InventoryData(this, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance());
        this.receiver = receiver;
    }

    @Override
    public void requestAllCards() {
        data.requestAllCards();
    }

    @Override
    public void requestUserCards() {
        data.requestUserCards();
    }

    @Override
    public void cardRequestCallback(List<Map<String, Object>> cardsRaw) {
        List<Card> cards = processCards(cardsRaw);
        this.receiver.receiveCardsResponse(cards);
    }

    @Override
    public List<Card> processCards(List<Map<String, Object>> cardsRaw) {
        List<Card> cards = new ArrayList<>();
        for (Map<String, Object> cardRaw : cardsRaw) {
            cards.add(new Card(
                    (String) cardRaw.get("name"),
                    (String) cardRaw.get("description"),
                    Card.Rarity.valueOf((String) cardRaw.get("rarity")),
                    (String) cardRaw.get("imageurl")
            ));
            Log.d("CARDRAW", (String) cardRaw.get("name"));
        }

        return cards;
    }

    @Override
    public void removeCard(Card card) {

    }

    @Override
    public void addCard(Card card) {

    }
}
