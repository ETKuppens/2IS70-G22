package com.example.cardhub.card_creation;

import android.net.Uri;
import android.util.Log;
import android.widget.RadioGroup;

import com.example.cardhub.R;
import com.example.cardhub.inventory.Card;
import com.google.android.material.textfield.TextInputEditText;

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

    public CardCreationState(CardCreationRepository repo) {
        repository = repo;
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
     * @pre isCardValid(getCard())
     */
    public void publishCard() {
        //Hook database
        Card sendableCard = getCard();
        if (isCardValid(sendableCard)) {
            repository.publishCard(sendableCard);
        } else {
            throw new IllegalArgumentException("Can't publish a card without specifying" +
                    "all properties first");
        }
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
     * Sets a new rarity to the current card.
     * @param rarity
     */
    public void setRarity(Card.Rarity rarity)
    {
        this.rarity = rarity;
    }

    /**
     * Checks the validity of a given card
     * @param c the card to check
     * @return true when card is valid
     */
    public boolean isCardValid(Card c)
    {
        if (c.IMAGE_URL == null)
        {
            return false;
        }
        if (c.NAME == null || c.NAME == "")
        {
            return false;
        }
        if (c.DESCRIPTION == null || c.DESCRIPTION == "")
        {
            return false;
        }
        if (c.RARITY == null)
        {
            return false;
        }
        return true;
    }
}
