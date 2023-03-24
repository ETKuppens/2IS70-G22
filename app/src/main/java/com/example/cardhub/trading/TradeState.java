package com.example.cardhub.trading;

import com.example.cardhub.Card;

import java.util.List;

/**
 * Keeps track of the current Trading Session, and updates both parties
 * @author Rijkman
 */
public class TradeState {
    TradingSession currentSession;

    public List<Card> getLocalSelectedCards() {
        return currentSession.getLocalSelection();
    }

    public void selectCard(Card c) {
        currentSession.selectCard(c);
    }

    public void deselectCard(Card c) {
        currentSession.selectCard(c);
    }
}