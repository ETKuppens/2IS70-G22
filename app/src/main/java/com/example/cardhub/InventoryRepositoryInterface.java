package com.example.cardhub;

public interface InventoryRepositoryInterface {
    public void getCards();
    public Card getCard(int i);
    public void removeCard(Card card);
    public void addCard(Card card);
}
