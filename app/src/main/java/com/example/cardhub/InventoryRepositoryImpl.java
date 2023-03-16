package com.example.cardhub;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class InventoryRepositoryImpl implements InventoryRepository {
    InventoryData data;
    InventoryState state;

    public InventoryRepositoryImpl(InventoryState state) {
        data = new InventoryData(this);
        this.state = state;
    }

    @Override
    public void requestCards() {
        data.requestCards();
    }

    @Override
    public void cardRequestCallback(List<Map<String, Object>> cardsRaw) {
        List<Card> cards = processCards(cardsRaw);
        state.setCards(cards);
    }

    @Override
    public List<Card> processCards(List<Map<String, Object>> cardsRaw) {
        List<Card> cards = new ArrayList<>();
        for (Map<String,Object> cardRaw : cardsRaw) {
            cards.add(new Card(
                    (String) cardRaw.get("name"),
                    (String) cardRaw.get("description"),
                    Card.Rarity.values()[(int)((long)cardRaw.get("rarity"))],
                    (String) cardRaw.get("imageurl")
            ));
            Log.d("CARDRAW", (String)cardRaw.get("name"));
        }

        return cards;
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
