package com.example.cardhub.trading;

import com.example.cardhub.Card;

import java.util.List;

/**
 * Stores two users trading, with their selected cards
 * @author Rijkman
 */
public class TradingSession {
    // User localUser
    // User remoteUser
    // User objects when they exist in the project

    private List<Card> localSelection;
    private List<Card> remoteSelection;

    public void setLocalSelection (List<Card> newSelection) {
        this.localSelection = newSelection;
    }

    public void setRemoteSelection (List<Card> newSelection) {
        this.remoteSelection = newSelection;
    }

    public List<Card> getLocalSelection () {
        return this.localSelection;
    }

    public List<Card> getRemoteSelection () {
        return this.remoteSelection;
    }
}
