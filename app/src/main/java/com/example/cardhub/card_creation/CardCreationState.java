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

    public void setSelectedImage(Uri newUri) {
        this.selectedImageUri = newUri;
    }
    public boolean hasSelectedImage() {
        return selectedImageUri == null;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public void setCurrentDescription(String currentDescription) {
        this.currentDescription = currentDescription;
    }

    public void setRarity(Card.Rarity rarity) {
        this.rarity = rarity;
    }
}
