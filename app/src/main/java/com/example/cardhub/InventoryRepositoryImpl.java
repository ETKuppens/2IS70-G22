package com.example.cardhub;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

public class InventoryRepositoryImpl implements InventoryRepository {
    InventoryData data;
    @Inject
    InventoryState state;

    public InventoryRepositoryImpl() {
        data = new InventoryData();
    }

    @Override
    public void requestCards(final Callback getCardsCallback) {
        data.requestCards(this::getCards, getCardsCallback, updateGridCallback);
    }

    @Override
    public void getCards(List<Map<String, Object>> cardsRaw, Consumer<List<Card>> callback) {
        List<Card> cards = new ArrayList<>();
        for (Map<String,Object> cardRaw : cardsRaw) {
            cards.add(new Card(
                    (String) cardRaw.get("name"),
                    (String) cardRaw.get("description"),
                    (Card.Rarity) cardRaw.get("rarity"),
                    (String) cardRaw.get("imageurl")
            ));
            Log.d("CARDRAW", (String)cardRaw.get("name"));
        }
        callback.accept(cards);
    }

    public class ProcessCardsCallback extends Callback {
        public void run(final List<Map<String, Object>> cardsRaw, final Callback callback) {

        }
    }


    @Override
    public void removeCard(Card card) {

    }

    @Override
    public void addCard(Card card) {

    }
}
