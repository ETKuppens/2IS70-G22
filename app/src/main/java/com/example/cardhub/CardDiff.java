package com.example.cardhub;

/**
 * ADT that encodes whether a given Card should be added or removed from the trade proposal.
 */
public class CardDiff {
    public enum DiffOption {
        ADD,
        REMOVE
    }

    final Card card;
    final DiffOption diff;

    CardDiff(Card card, DiffOption diff) {
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
}
