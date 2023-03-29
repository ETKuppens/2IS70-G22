package com.example.cardhub.TradingMode;

import com.example.cardhub.Card;

import java.util.HashSet;
import java.util.Set;

/**
 * ADT for which cards are offered in a current trading session.
 * Should be updated by both the current UI instance, and the server.
 */
public class TradingSession {
    public Set<Card> thisUserProposedCards = new HashSet<Card>();  // Set of cards that are proposed by the current application instance.
    public Set<Card> otherUserProposedCards = new HashSet<Card>(); // Set of cards that are proposed by the other application instance.

    /**
     * Change the current proposed cards for this user.
     * @param diffs a set of card differences that should be applied to the proposal.
     * @pre {@code \forall(d; diffs.contains(d);
     *         ((d.getDiff() == CardDiff.ADD) -> (!thisUserProposedCards.contains(d.getCard()))) &&
     *         ((d.getDiff() == CardDiff.REMOVE) -> (thisUserProsedCards.contains(d.getCard()))
     *         )}
     * @throws IllegalArgumentException when !\forall(d; diffs.contains(d);
     *         ((d.getDiff() == CardDiff.ADD) -> (!thisUserProposedCards.contains(d.getCard()))) &&
     *         ((d.getDiff() == CardDiff.REMOVE) -> (thisUserProsedCards.contains(d.getCard()))
     *         )}
     */
    public void AddCardDiffsForThisUser(Set<CardDiff> diffs) {
        // Check for the precondition
        for (CardDiff d : diffs) {
            switch (d.getDiff()) {
                case ADD:
                    if (this.thisUserProposedCards.contains(d.getCard())) {
                        throw new IllegalArgumentException("TradingSession.AddCardDiffsForThisUser: " +
                                "a card should be added to this client instances trade proposal, " +
                                "but the card was already added.");
                    }
                    break;

                case REMOVE:
                    if (!this.thisUserProposedCards.contains(d.getCard())) {
                        throw new IllegalArgumentException("TradingSession.AddCardDiffsForThisUser: " +
                                "a card should be removed from this client instances trade proposal, " +
                                "but the card is not currently in the proposal.");
                    }
                    break;
            }
        }

        // Apply each CardDiff to the thisUserProposedCards
        for (CardDiff d : diffs) {
            switch(d.getDiff()) {
                case ADD:
                    AddCardToThisUserProposedCards(d.getCard());
                    break;

                case REMOVE:
                    RemoveCardFromThisUserProposedCards(d.getCard());
                    break;
            }
        }
    }

    /**
     * Change the current proposed cards for the other user.
     * @param diffs a set of card differences that should be applied to the proposal.
     * @pre {@code \forall(d; diffs.contains(d);
     *         ((d.getDiff() == CardDiff.ADD) -> (!otherUserProposedCards.contains(d.getCard()))) &&
     *         ((d.getDiff() == CardDiff.REMOVE) -> (otherUserProposedCards.contains(d.getCard()))
     *         )}
     * @throws IllegalArgumentException when !\forall(d; diffs.contains(d);
     *         ((d.getDiff() == CardDiff.ADD) -> (!otherUserProposedCards.contains(d.getCard()))) &&
     *         ((d.getDiff() == CardDiff.REMOVE) -> (otherUserProposedCards.contains(d.getCard()))
     *         )}
     */
    public void AddCardDiffsForOtherUser(Set<CardDiff> diffs) {
        // Check for the precondition
        for (CardDiff d : diffs) {
            switch (d.getDiff()) {
                case ADD:
                    if (this.otherUserProposedCards.contains(d.getCard())) {
                        throw new IllegalArgumentException("TradingSession.AddCardDiffsForOtherUser: " +
                                "a card should be added to the other client instances trade proposal, " +
                                "but the card was already added.");
                    }
                    break;

                case REMOVE:
                    if (!this.otherUserProposedCards.contains(d.getCard())) {
                        throw new IllegalArgumentException("TradingSession.AddCardDiffsForThisUser: " +
                                "a card should be removed from the other client instances trade proposal, " +
                                "but the card is not currently in the proposal.");
                    }
                    break;
            }
        }

        // Apply each CardDiff to the thisUserProposedCards
        for (CardDiff d : diffs) {
            switch(d.getDiff()) {
                case ADD:
                    AddCardToOtherUserProposedCards(d.getCard());
                    break;

                case REMOVE:
                    RemoveCardFromOtherUserProposedCards(d.getCard());
                    break;
            }
        }
    }


    /**
     * Add a new card to the list of cards that this current application instance proposes to trade.
     *
     * @param newProposedCard the new card that this user proposes to trade.
     * @pre {@code !this.thisUserProposedCards.contains(newProposedCard)}
     * @throws IllegalArgumentException when this.thisUserProposedCards.contains(newProposedCard)
     */
    private void AddCardToThisUserProposedCards(Card newProposedCard) {
        if (this.thisUserProposedCards.contains(newProposedCard)) {
            throw new IllegalArgumentException("TradingSession.AddCardToThisUserProposedCards: " +
                    "the newProposedCard is already an element of thisUserProposedCards.");
        }

        this.thisUserProposedCards.add(newProposedCard);
    }

    /**
     * Add a set of new cards to the list of cards that this current application instance proposes
     * to trade.
     *
     * @param newProposedCards a set of cards that this user proposes to trade.
     * @pre {@code !\exists(c; newProposedCards.contains(c); this.thisUserProposedCards.contains(c)}
     * @throws IllegalArgumentException when \exists(c; newProposedCards.contains(c); this.thisUserProposedCards.contains(c)
     */
    private void AddMultipleCardsToThisUserProposedCards(Set<Card> newProposedCards) {
        for (Card card : newProposedCards) {
            if (this.thisUserProposedCards.contains(card)) {
                throw new IllegalArgumentException("TradingSession.AddMultipleCardsToThisUserProposedCards: " +
                        "one of the newly proposed cards is already an element of thisUserProposedCards.");
            }
        }

        this.thisUserProposedCards.addAll(newProposedCards);
    }


    /**
     * Add a new card to the list of cards that the other application instance proposes to trade.
     *
     * @param newProposedCard the new card that the other user proposes to trade.
     * @pre {@code !this.otherUserProposedCards.contains(newProposedCard)}
     * @throws IllegalArgumentException when this.otherUserProposedCards.contains(newProposedCard)
     */
    private void AddCardToOtherUserProposedCards(Card newProposedCard) {
        if (this.otherUserProposedCards.contains(newProposedCard)) {
            throw new IllegalArgumentException("TradingSession.AddCardToOtherUserProposedCards: " +
                    "the newProposedCard is already an element of otherUserProposedCards.");
        }

        this.otherUserProposedCards.add(newProposedCard);
    }

    /**
     * Add a set of new cards to the list of cards that the other application instance proposes
     * to trade.
     *
     * @param newProposedCards a set of cards that the other user proposes to trade.
     * @pre {@code !\exists(c; newProposedCards.contains(c); this.otherUserProposedCards.contains(c)}
     * @throws IllegalArgumentException when \exists(c; newProposedCards.contains(c); this.otherUserProposedCards.contains(c)
     */
    private void AddMultipleCardsToOtherUserProposedCards(Set<Card> newProposedCards) {
        for (Card card : newProposedCards) {
            if (this.otherUserProposedCards.contains(card)) {
                throw new IllegalArgumentException("TradingSession.AddMultipleCardsToOtherUserProposedCards: " +
                        "one of the newly proposed cards is already an element of otherUserProposedCards.");
            }
        }

        this.otherUserProposedCards.addAll(newProposedCards);
    }


    /**
     * Remove a card from the list of cards that this current application instance proposes to trade.
     *
     * @param proposedCard the card that this user removes from the trade proposal.
     * @pre {@code this.thisUserProposedCards.contains(proposedCard)}
     * @throws IllegalArgumentException when !this.thisUserProposedCards.contains(proposedCard)
     */
    private void RemoveCardFromThisUserProposedCards(Card proposedCard) {
        if (!this.thisUserProposedCards.contains(proposedCard)) {
            throw new IllegalArgumentException("TradingSession.RemoveCardFromThisUserProposedCards: " +
                    "the proposedCard is not an element of thisUserProposedCards.");
        }

        this.thisUserProposedCards.remove(proposedCard);
    }

    /**
     * Remove a set of new cards from the list of cards that this current application instance proposes
     * to trade.
     *
     * @param proposedCards a set of cards that this user removes from the trade proposal.
     * @pre {@code \forall(c; newProposedCards.contains(c); this.thisUserProposedCards.contains(c)}
     * @throws IllegalArgumentException when !\forall(c; newProposedCards.contains(c); this.thisUserProposedCards.contains(c)
     */
    private void RemoveMultipleCardsFromThisUserProposedCards(Set<Card> proposedCards) {
        for (Card card : proposedCards) {
            if (!this.thisUserProposedCards.contains(card)) {
                throw new IllegalArgumentException("TradingSession.RemoveMultipleCardsFromThisUserProposedCards: " +
                        "one of the proposed cards to remove is not an element of thisUserProposedCards.");
            }
        }

        this.thisUserProposedCards.removeAll(proposedCards);
    }


    /**
     * Remove a card from the list of cards that the other application instance proposes to trade.
     *
     * @param proposedCard the card that the other user removes from the trade proposal.
     * @pre {@code this.otherUserProposedCards.contains(proposedCard)}
     * @throws IllegalArgumentException when !this.otherUserProposedCards.contains(proposedCard)
     */
    private void RemoveCardFromOtherUserProposedCards(Card proposedCard) {
        if (!this.otherUserProposedCards.contains(proposedCard)) {
            throw new IllegalArgumentException("TradingSession.RemoveCardFromOtherUserProposedCards: " +
                    "the proposedCard is not an element of otherUserProposedCards.");
        }

        this.otherUserProposedCards.remove(proposedCard);
    }

    /**
     * Remove a set of new cards from the list of cards that the other application instance proposes
     * to trade.
     *
     * @param proposedCards a set of cards that the other user removes from the trade proposal.
     * @pre {@code \forall(c; newProposedCards.contains(c); this.otherUserProposedCards.contains(c)}
     * @throws IllegalArgumentException when !\forall(c; newProposedCards.contains(c); this.otherUserProposedCards.contains(c)
     */
    private void RemoveMultipleCardsFromOtherUserProposedCards(Set<Card> proposedCards) {
        for (Card card : proposedCards) {
            if (!this.otherUserProposedCards.contains(card)) {
                throw new IllegalArgumentException("TradingSession.RemoveMultipleCardsFromOtherUserProposedCards: " +
                        "one of the newly proposed cards is not an element of otherUserProposedCards.");
            }
        }

        this.otherUserProposedCards.removeAll(proposedCards);
    }
}
