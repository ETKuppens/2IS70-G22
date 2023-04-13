package com.example.cardhub.inventory;

import java.util.List;

public class CardSorter {
    /**
     * Enum for different attributes a card can be sorted on.
     */
    public enum SortAttribute {
        NAME,
        RARITY
    }

    /**
     * Enum for the ordering type when sorting.
     */
    public enum SortOrder {
        DEFAULT, // Sort in the default order
        REVERSE  // Sort in the reverse order
    }

    /**
     * Sort a list of cards based on a Card.SortAttribute.
     * @modifies cards
     */
    public static void Sort(List<Card> cards, SortAttribute sortAttribute, SortOrder sortOrder) {
        switch(sortAttribute) {
            case NAME:
                SortOnName(cards, sortOrder);
                break;

            case RARITY:
                SortOnRarity(cards, sortOrder);
                break;
        }
    }

    /**
     * Sort a list of cards using the correct comparison function, based on the given SortOrder.
     * @modifies cards
     */
    private static void SortOnName(List<Card> cards, SortOrder sortOrder) {
        switch(sortOrder) {
            case DEFAULT:
                SortOnNameDefaultOrder(cards);
                break;

            case REVERSE:
                SortOnNameReverseOrder(cards);
                break;
        }
    }

    /**
     * Sort a list of cards based on their names, alphabetically, in default order.
     * @modifies cards
     */
    private static void SortOnNameDefaultOrder(List<Card> cards) {
        // Sort the cards on their name alphabetically, ignoring upper/lower case, in default order.
        cards.sort((c1, c2) -> c1.NAME.compareToIgnoreCase(c2.NAME));
    }

    /**
     * Sort a list of cards based on their names, alphabetically, in reverse order.
     * @modifies cards
     */
    private static void SortOnNameReverseOrder(List<Card> cards) {
        // Sort the cards on their name alphabetically, ignoring upper/lower case, in reverse order.
        cards.sort((c1, c2) -> (c1.NAME.compareToIgnoreCase(c2.NAME)) * -1);
    }

    /**
     * Compare two Card Rarities.
     * @param leftRarity the card rarity on the left side of the equation
     * @param rightRarity the card rarity on the right side of the equation
     * @return -1 if leftRarity < rightRarity
     * @return 0 if leftRarity == rightRarity
     * @return 1 if leftRarity > rightRarity
     */
    private static int CompareRarities(Card.Rarity leftRarity, Card.Rarity rightRarity) {
        // Check if leftRarity == rightRarity,
        // and return 0 if this is the case
        if (leftRarity.equals(rightRarity)) {
            return 0;
        }
        // leftRarity != rightRarity

        // Check if leftRarity < rightRarity,
        // and return -1 if this is the case
        boolean isStrictlySmaller = false;

        switch (leftRarity) {
            case ULTRA_RARE:
                isStrictlySmaller = !rightRarity.equals(Card.Rarity.ULTRA_RARE);
                break;

            case COMMON:
                isStrictlySmaller = rightRarity.equals(Card.Rarity.RARE)
                        || rightRarity.equals(Card.Rarity.LEGENDARY);
                break;

            case RARE:
                isStrictlySmaller = rightRarity.equals(Card.Rarity.LEGENDARY);
                break;

            case LEGENDARY:
                isStrictlySmaller = false;
                break;
        }

        if (isStrictlySmaller) {
            return -1;
        }
        // leftRarity > rightRarity

        return 1;
    }

    /**
     * Sort a list of cards based on their rarity, in default order,
     * ignoring their names.
     * @modifies cards
     */
    private static void SortOnRarityDefaultOrder(List<Card> cards) {
        // Sort the cards based on their rarity
        cards.sort((c1, c2) -> CompareRarities(c1.RARITY, c2.RARITY));
    }

    /**
     * Sort a list of cards based on their rarity, in default order,
     * ignoring their names.
     * @modifies cards
     */
    private static void SortOnRarityReverseOrder(List<Card> cards) {
        // Sort the cards based on their rarity
        cards.sort((c1, c2) -> CompareRarities(c1.RARITY, c2.RARITY) * -1);
    }

    /**
     * Sort a list of cards based on their rarity,
     * and make sure that within their rarities, the cards are
     * also sorted on their names.
     * @modifies cards
     */
    private static void SortOnRarity(List<Card> cards, SortOrder sortOrder) {
        switch(sortOrder) {
            case DEFAULT:
                SortOnRarityDefaultOrder(cards);
                break;

            case REVERSE:
                SortOnRarityReverseOrder(cards);
                break;
        }

        // For each card group of the same rarity, sort the cards on their name alphabetically.
        int fromIndex = 0;
        int toIndex = 0;
        Card.Rarity currentGroupRarity = null;

        for (Card card : cards) {
            // Initialize the currentGroupRarity on the first iteration.
            if (currentGroupRarity == null) {
                currentGroupRarity = card.RARITY;
            }

            // If the rarity of the current card does not match the rarity of the current group
            // that is being looked at, get a sublist containing all cards of the current group,
            // sort that sublist on name alphabetically, and update the currentGroupRarity and
            // fromIndex.
            if (!card.RARITY.equals(currentGroupRarity)) {
                List<Card> group = cards.subList(fromIndex, toIndex);

                SortOnName(group, SortOrder.DEFAULT);

                currentGroupRarity = card.RARITY;
                fromIndex = toIndex;
            }

            // Increment the toIndex to point at the index of the current card (inclusive)
            toIndex++;
        }

        // Sort the last group on name alphabetically.
        List<Card> group = cards.subList(fromIndex, toIndex);

        SortOnName(group, SortOrder.DEFAULT);
    }
}
