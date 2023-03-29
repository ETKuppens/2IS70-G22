package com.example.cardhub.card_creation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.cardhub.Card;
import com.example.cardhub.R;
import com.example.cardhub.inventory.CardActivity;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Activity that allows a creator user to create custom cards.
 * @author Rijkman
 */
public class CardCreationActivity extends AppCompatActivity {
    //Keeps track of the current state variable
    CardCreationState state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_creation);

        state = new CardCreationState();

        //Object to prompt the user to pick an image
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        state.setSelectedImage(uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        //Button that prompts the user with the image chooser
        Button imageButton = (Button) findViewById(R.id.selectImageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType((ActivityResultContracts.PickVisualMedia.VisualMediaType) ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        //Button that allows to preview Card
        Button previewCardButton = (Button) findViewById(R.id.previewCardButton);
        previewCardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkCardValidity();

                Intent intent = new Intent(getApplicationContext(), CardActivity.class);
                intent.putExtra("card", state.getCard());
                startActivity(intent);
            }
        });

        //Button that allows a creator to add the current card to the card pool
        Button cardCreateButton = (Button) findViewById(R.id.createCardButton);
        cardCreateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Add this card to the database
                if (!checkCardValidity()) {
                    return;
                }

                state.publishCard();
            }
        });
    }

    private boolean checkCardValidity() {
        //First check if everything is specified
        TextInputEditText cardName = (TextInputEditText)(findViewById(R.id.cardNameInput));
        if (cardName.getText().equals("")) {
            return false;
        }
        state.setCurrentName(cardName.getText().toString());

        TextInputEditText cardDescription = (TextInputEditText) (findViewById((R.id.cardDescriptionInput)));
        if (cardDescription.getText().equals("")) {
            return false;
        }
        state.setCurrentDescription(cardDescription.getText().toString());

        //Rarity defaults to common
        RadioGroup rarityButtons = (RadioGroup)findViewById(R.id.rarityButtons);
        switch(rarityButtons.getCheckedRadioButtonId()) {
            case R.id.commonButton: {
                state.setRarity(Card.Rarity.COMMON);
            }
            case R.id.rareButton: {
                state.setRarity(Card.Rarity.RARE);
            }
            case R.id.legendaryButton: {
                state.setRarity(Card.Rarity.LEGENDARY);
            }
            default: {
                state.setRarity(Card.Rarity.UNKNOWN);
            }
        }

        //Check if there is a selected image
        if (!state.hasSelectedImage()) {
            return false;
        }

        return true;
    }
}