package com.example.cardhub.card_creation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.cardhub.PairingMode.PairingModeActivity;
import com.example.cardhub.R;
import com.example.cardhub.authentification.LoginActivity;
import com.example.cardhub.creator_navigation.CreatorBaseActivity;
import com.example.cardhub.inventory.Card;
import com.example.cardhub.inventory.CardActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

/**
 * Activity that allows a creator user to create custom cards.
 *
 * @author Rijkman
 */
public class CardCreationActivity extends CreatorBaseActivity {
    //Authorizaton instance
    private FirebaseAuth mAuth;
    //Keeps track of the current state variable
    CardCreationState state;

    @Override
    public int getLayoutId() {
        return R.layout.activity_card_creation;
    }

    @Override
    public int getBottomNavigationMenuItemId() {
        return R.id.action_create;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_creation);

        //Set up the navigation bar on the bottom of the screen
        setupNav();
        state = new CardCreationState(new CardCreationRepositoryImpl());
        mAuth = FirebaseAuth.getInstance();

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
                Card c = state.getCard();
                Gson converter = new Gson();
                String encodedCard = converter.toJson(c);
                intent.putExtra("card", encodedCard);
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
                    Log.d("CARD_CREATION", "card invalid");
                    return;
                }

                state.publishCard();
                Toast.makeText(CardCreationActivity.this, "Card has been created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Function that verifies the validity of the currently created card object.
     * @return true if object is valid
     */
    private boolean checkCardValidity() {
        //First check if cardName is specified
        TextInputEditText cardName = findViewById(R.id.cardNameInput);
        if (cardName.getText().equals("")) {
            Log.d("CARD_CREATION", "name invalid");
            return false;
        }
        state.setCurrentName(cardName.getText().toString());

        //Check if card description is not empty
        TextInputEditText cardDescription = findViewById(R.id.cardDescriptionInput);
        if (cardDescription.getText().equals("")) {
            Log.d("CARD_CREATION", "description invalid");
            return false;
        }
        state.setCurrentDescription(cardDescription.getText().toString());

        //Rarity defaults to common
        RadioGroup rarityButtons = findViewById(R.id.rarityButtons);
        switch(rarityButtons.getCheckedRadioButtonId()) {
            case R.id.commonButton: {
                state.setRarity(Card.Rarity.COMMON);
                break;
            }
            case R.id.rareButton: {
                state.setRarity(Card.Rarity.RARE);
                break;
            }
            case R.id.legendaryButton: {
                state.setRarity(Card.Rarity.LEGENDARY);
                break;
            }
            case R.id.ultraRareButton: {
                state.setRarity(Card.Rarity.ULTRA_RARE);
                break;
            }
            default: {
                state.setRarity(Card.Rarity.COMMON);
                break;
            }
        }

        //Check if there is a selected image
        if (!state.hasSelectedImage()) {
            Log.d("CARD_CREATION", "image not selected");
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            this.startActivity(intent);
            startActivity(intent);
        }
    }
}