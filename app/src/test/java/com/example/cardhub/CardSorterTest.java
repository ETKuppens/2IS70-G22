package com.example.cardsprototype;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CardSorterTest {
    /**
     * TODO: make contracts formal.
     */

    /**
     * Generate an unsorted list of 8 cards.
     */
    private List<Card> GetUnsortedCards() {
        String name = "c1";
        String description = "description1";
        Card.Rarity rarity = Card.Rarity.COMMON;
        int image = R.drawable.amongsus;
        Card c1 = new Card(name, description, rarity, image);

        name = "c2";
        description = "description2";
        rarity = Card.Rarity.COMMON;
        Card c2 = new Card(name, description, rarity, image);

        name = "duplicatename";
        description = "description3";
        rarity = Card.Rarity.COMMON;
        Card c3 = new Card(name, description, rarity, image);

        name = "duplicatename";
        description = "description4";
        rarity = Card.Rarity.RARE;
        Card c4 = new Card(name, description, rarity, image);

        name = "duplicatename";
        description = "description5";
        rarity = Card.Rarity.LEGENDARY;
        Card c5 = new Card(name, description, rarity, image);

        name = "differentnamelength";
        description = "description6";
        rarity = Card.Rarity.COMMON;
        Card c6 = new Card(name, description, rarity, image);

        name = "nameofevenadifferentlengthandrare";
        description = "description7";
        rarity = Card.Rarity.RARE;
        Card c7 = new Card(name, description, rarity, image);

        name = "anothernamelengthandlegendary";
        description = "description3";
        rarity = Card.Rarity.LEGENDARY;
        Card c8 = new Card(name, description, rarity, image);

        return Arrays.asList(c2, c1, c3, c4, c5, c6, c7, c8);
    }

    /**
     * Check whether a list of Strings is in alphabetical order.
     * @param listToCheck the list to check whether it is ordered alphabetically.
     */
    private boolean OrderedAlphabetically(List<String> listToCheck) {
        // The list is always ordered if it contains at most one element.
        if (listToCheck.size() == 0 || listToCheck.size() == 1) {
            return true;
        }
        // listToCheck.size() > 1
        // Compare the elements for order

        // String variables that will be used to compare elements in listToCheck
        String leftString = null;
        String rightString = null;

        // Go through every element in listToCheck, and
        // check whether it is in order with the element that
        // comes before it.
        for (String element : listToCheck) {
            rightString = element;

            // If at least two elements in listToCheck have
            // been read already.
            // (two strings are needed for comparison)
            if (leftString != null) {
                // Compare the two elements
                int comparison = leftString.compareToIgnoreCase(rightString);

                // Check if the leftString has a greater lexiconic value compared
                // to the rightString.
                // If this is the case, the leftString is not ordered compared to
                // the rightString; the listToCheck is not ordered.
                if (comparison > 0) {
                    return false;
                }
            }

            leftString = rightString;
        }
        // All neighbouring elements are in order
        // ==> the entire list is in order.

        return true;
    }

    /**
     * Check whether rarities in a list are grouped together; for any two different elements of
     * the same rarity, there is no element of another rarity between them.
     * @param listToCheck the list to check whether its rarities are grouped together
     */
    private boolean RaritiesGroupedTogether(List<Card.Rarity> listToCheck) {
        // The list is always grouped correctly if it contains at most one element.
        if (listToCheck.size() == 0 || listToCheck.size() == 1) {
            return true;
        }
        // listToCheck.size() > 1
        // Compare the elements for groupings

        // Set of rarity groups that have been found before.
        Set<Card.Rarity> checkedRarities = new HashSet<Card.Rarity>();

        // Rarity variables that will be used to compare elements in listToCheck
        Card.Rarity leftRarity = null;
        Card.Rarity rightRarity = null;

        // Go through every element in listToCheck, and
        // check whether it is in a valid group.
        for (Card.Rarity element : listToCheck) {
            rightRarity = element;

            // Check whether the leftRarity is a rarity that shouldn't have reoccured,
            // in which case the rarities are not grouped together correctly.
            if (checkedRarities.contains(rightRarity)) {
                return false;
            }

            // If at least two elements in listToCheck have
            // been read already,
            // and the rarities differ.
            if (leftRarity != null && !leftRarity.equals(rightRarity)) {
                // Add the leftRarity to the set of rarities that shouldn't reoccur.
                checkedRarities.add(leftRarity);
            }

            leftRarity = rightRarity;
        }

        return true;
    }

    /**
     * Check whether a list of cards that are grouped together by rarity are sorted on name
     * in alphabetical order for each group.
     * @param listToCheck a list of cards to check
     * @pre Cards in listToCheck are grouped together by Card.Rarity.
     */
    private boolean EveryGroupOrderedAlphabetically(List<Card> listToCheck) {
        // Group every card by rarity, and check whether the names of the cards are sorted
        // alphabetically within each group.
        List<String> groupCardNames = new ArrayList<String>();
        Card.Rarity currentGroupRarity = null;

        for (Card card : listToCheck) {
            // If this card has a rarity that does not equal the current group,
            // check if the current group is ordered alphabetically, and flush
            // the list holding the card names.
            if (!card.RARITY.equals(currentGroupRarity)) {
                if (!OrderedAlphabetically(groupCardNames)) {
                    return false;
                }

                // Create a new group card names list.
                groupCardNames = new ArrayList<String>();

                // Set the current group rarity to the newly found rarity.
                currentGroupRarity = card.RARITY;
            }

            // Add the name of the current card to the groupCardNames
            groupCardNames.add(card.NAME);
        }

        // Check if the last group is ordered alphabetically.
        if (!OrderedAlphabetically(groupCardNames)) {
            return false;
        }

        return true;
    }

    /**
     * Compare two Card Rarities; check if leftRarity <= rightRarity.
     * @param leftRarity the rarity on the left side of the equation
     * @param rightRarity the rarity on the left side of the equation
     */
    private boolean RaritySmallerEquals(Card.Rarity leftRarity, Card.Rarity rightRarity) {
        switch(leftRarity) {
            case UNKNOWN:
                return true;
            case COMMON:
                return !rightRarity.equals(Card.Rarity.UNKNOWN);
            case RARE:
                return rightRarity.equals(Card.Rarity.RARE)
                        || rightRarity.equals(Card.Rarity.LEGENDARY);
            case LEGENDARY:
                return rightRarity.equals(Card.Rarity.LEGENDARY);
        }

        throw new RuntimeException("CardSorterTest.RaritySmallerEquals: switch case did not " +
                "return a value");
    }

    /**
     * Check whether a list of rarities is ordered correctly.
     * @param listToCheck the list of rarities the check for order
     */
    private boolean OrderedRarities(List<Card.Rarity> listToCheck) {
        // The list is always ordered if it contains at most one element.
        if (listToCheck.size() == 0 || listToCheck.size() == 1) {
            return true;
        }
        // listToCheck.size() > 1
        // Compare the elements for order

        // String variables that will be used to compare elements in listToCheck
        Card.Rarity leftRarity = null;
        Card.Rarity rightRarity = null;

        // Go through every element in listToCheck, and
        // check whether it is in order with the element that
        // comes before it.
        for (Card.Rarity element : listToCheck) {
            rightRarity = element;

            // If at least two elements in listToCheck have
            // been read already.
            // (two rarities are needed for comparison)
            if (leftRarity != null) {
                // Compare the two elements
                if (!RaritySmallerEquals(leftRarity, rightRarity)) {
                    return false;
                }
            }

            leftRarity = rightRarity;
        }
        // All neighbouring elements are in order
        // ==> the entire list is in order.

        return true;
    }

    @Test
    public void SortTestOnName() {
        // Create a list of cards, unsorted by the name attribute.
        List<Card> cards = GetUnsortedCards();

        // Sort the list of unsorted cards by the name attribute.
        CardSorter.Sort(cards, CardSorter.SortAttribute.NAME);

        // Copy the names of all cards in the sorted cards list to a new list of strings,
        // maintaining the order of cards in the sorted cards list.
        List<String> namesList = new ArrayList<String>();

        for (Card card : cards) {
            namesList.add(card.NAME);
        }

        // Test whether the names of the cards are in alphabetical order.
        assertTrue("SortTestOnName: OrderedAlphabetically",
                OrderedAlphabetically(namesList));
    }

    @Test
    public void SortTestOnRarity() {
        // Create a list of cards, unsorted by the rarity attribute.
        List<Card> cards = GetUnsortedCards();

        // Sort the list of unsorted cards by the rarity attribute.
        CardSorter.Sort(cards, CardSorter.SortAttribute.RARITY);

        // Copy the rarities of all cards in the sorted cards list to a new list of rarities,
        // maintaining the order of cards in the sorted cards list.
        List<Card.Rarity> raritiesList = new ArrayList<Card.Rarity>();

        for (Card card : cards) {
            raritiesList.add(card.RARITY);
        }

        // Test whether the rarities are grouped together in the sorted cards list.
        assertTrue("SortTestOnRarity: RaritiesGroupedTogether",
                RaritiesGroupedTogether(raritiesList));

        // Test whether every group of rarities is alphabetically sorted on name individually.
        assertTrue("SortTestOnRarity: EveryGroupOrderedAlphabetically",
                EveryGroupOrderedAlphabetically(cards));

        // Test whether every rarity is in the right order.
        assertTrue("SortTestOnRarity: OrderedRarities", OrderedRarities(raritiesList));
    }
}