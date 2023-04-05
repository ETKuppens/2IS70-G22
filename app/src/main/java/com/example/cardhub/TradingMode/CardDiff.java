package com.example.cardhub.TradingMode;

import com.example.cardhub.inventory.Card;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * ADT that encodes whether a given Card should be added or removed from the trade proposal.
 */
public class CardDiff implements Serializable {
    public enum DiffOption {
        ADD,
        REMOVE
    }

    final Card card;
    final DiffOption diff;

    public CardDiff(Card card, DiffOption diff) {
        this.card = card;
        this.diff = diff;
    }

    /**
     * Get the card that should be either added or removed from the current TradingSession.
     *
     * @return the card that should be either added or removed from the current TradingSession.
     */
    public Card getCard() {
        return this.card;
    }

    /**
     * Get whether the card of this CardDiff should be either added or removed from the current
     * TradingSession.
     * @return the DiffOption that encodes whether the card of this CardDiff should be added or
     * removed from the current TradingSession.
     */
    public DiffOption getDiff() {
        return this.diff;
    }

    public Map<String, Object> serialize () {
        Map<String, Object> data = new HashMap<>();

        data.put("card", this.card.serialize());
        data.put("diff", this.diff);

        return data;
    }

}
