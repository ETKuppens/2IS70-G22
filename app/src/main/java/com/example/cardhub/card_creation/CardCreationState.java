package com.example.cardhub.card_creation;

import android.net.Uri;

import com.example.cardhub.inventory.Card;

/**
 * Keeps track of the card that is currently being created.
 * @author Rijkman
 */
public class CardCreationState {
    //Stores the card's image
    private Uri selectedImageUri;
    //Stores the card's custom name
    private String currentName;
    //Stores the card's custom description
    private String currentDescription;
    //Stores the card's custom rarity
    private Card.Rarity rarity;

    //Repository for this class
    private CardCreationRepository repository;

    public CardCreationState() {
        repository = new CardCreationRepository();
    }

    /**
     * Constructs a card object from the given information in the card editor.
     * Assumes that the values for the card are correct and filled in.
     * @return the card object
     */
    public Card getCard() {
        return new Card(currentName, currentDescription, rarity, selectedImageUri.toString());
    }

    /**
     * Method that publishes the current card to the card pool.
     */
    public void publishCard() {
        //Hook database
        Card sendableCard = getCard();
        repository.publishCard(sendableCard);
    }

    /**
     * Sets the image for the card being currently created.
     * @param newUri the Uri to the image of the new card
     */
    public void setSelectedImage(Uri newUri) {
        this.selectedImageUri = newUri;
    }

    /**
     * Checks whether the card being currently created has a specified image.
     * @return true when the card has a specified image
     */
    public boolean hasSelectedImage() {
        return selectedImageUri != null;
    }

    /**
     * Sets the name of the card being currently created.
     * @param currentName the new name of the card
     */
    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    /**
     * Sets the description of the card being currently created.
     * @param currentDescription the new description of the card
     */
    public void setCurrentDescription(String currentDescription) {
        this.currentDescription = currentDescription;
    }

    /**
     * Sets the rarity of the card being currently created.
     * @param rarity the new rarity of the card.
     */
    public void setRarity(Card.Rarity rarity) {
        this.rarity = rarity;
    }
}
