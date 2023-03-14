package com.example.cardhub;

import java.util.List;

public interface GetCardsCallback {
    public void run(final List<Card> cards);
}
