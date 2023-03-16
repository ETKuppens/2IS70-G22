package com.example.cardhub;

import java.util.List;

public interface InventoryState {
    public void setCards(List<Card> cards);

    public void requestCards();

    public List<Card> getCards();

}
