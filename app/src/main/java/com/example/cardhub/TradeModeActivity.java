package com.example.cardhub;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cardhub.TradingMode.CardDiff;
import com.example.cardhub.TradingMode.OnRecyclerViewItemClickListener;
import com.example.cardhub.TradingMode.TradeModeState;
import com.example.cardhub.TradingMode.TradingSession;
import com.example.cardhub.inventory.CardActivity;
import com.example.cardhub.inventory.InventoryActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TradeModeActivity extends AppCompatActivity implements View.OnClickListener, OnRecyclerViewItemClickListener {

    private TradeModeState state;

    private Button readyButton;
    private Button cancelButton;
    private Button cardSelectButton;

    private TextView otherPlayerReadyText;

    private RecyclerView otherPlayerProposedCardsRecyclerView;
    private RecyclerView thisPlayerProposedCardsRecyclerView;

    private CardRecyclerViewAdapter otherPlayerRecyclerViewAdapter;
    private CardRecyclerViewAdapter thisPlayerRecyclerViewAdapter;

    private List<Card> otherPlayerProposedCards = new ArrayList<>();
    private List<Card> thisPlayerProposedCards = new ArrayList<>();

    private Card clickedCard = null; // Card that was clicked to be removed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_mode);

        readyButton = findViewById(R.id.ReadyButton);
        cancelButton = findViewById(R.id.CancelButton);
        cardSelectButton = findViewById(R.id.CardSelectButton);

        readyButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        cardSelectButton.setOnClickListener(this);

        otherPlayerReadyText = findViewById(R.id.PlayerReadyToTradeTextView);
        this.disableOtherPlayerReadyMessage();

        state = new TradeModeState(this);

        otherPlayerProposedCardsRecyclerView = findViewById(R.id.ProposedCardsOtherPlayer);
        otherPlayerProposedCardsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        otherPlayerRecyclerViewAdapter = new CardRecyclerViewAdapter(getApplicationContext(), otherPlayerProposedCards);
        otherPlayerProposedCardsRecyclerView.setAdapter(otherPlayerRecyclerViewAdapter);

        thisPlayerProposedCardsRecyclerView = findViewById(R.id.ProposedCardsThisPlayer);
        thisPlayerProposedCardsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        thisPlayerRecyclerViewAdapter = new CardRecyclerViewAdapter(getApplicationContext(), thisPlayerProposedCards);
        thisPlayerProposedCardsRecyclerView.setAdapter(thisPlayerRecyclerViewAdapter);
        thisPlayerRecyclerViewAdapter.setOnRecyclerViewItemClickListener(this);
    }

    /**
     * Update the UI in activity using the data from the TradingSession instance.
     * TODO: implement this method
     */
    public void updateUI(TradingSession tradingSession) {
        if (otherPlayerProposedCards == null) {
            this.otherPlayerProposedCards = new ArrayList<>(tradingSession.otherUserProposedCards);
            this.thisPlayerProposedCards = new ArrayList<>(tradingSession.thisUserProposedCards);
        } else {
            this.otherPlayerProposedCards.clear();

            for (Card card : tradingSession.otherUserProposedCards) {
                otherPlayerProposedCards.add(card);
            }

            this.thisPlayerProposedCards.clear();

            for (Card card : tradingSession.thisUserProposedCards) {
                thisPlayerProposedCards.add(card);
            }
        }

        this.otherPlayerRecyclerViewAdapter.notifyDataSetChanged();
        this.thisPlayerRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void cancelTradeMode() {
        finish();
    }

    public void disableChangeTradeProposal() {
        this.cardSelectButton.setEnabled(false);
    }

    public void enableChangeTradeProposal() {
        this.cardSelectButton.setEnabled(true);
    }

    public void disableAcceptTrade() {
        this.readyButton.setEnabled(false);
    }

    public void enableAcceptTrade() {
        this.readyButton.setEnabled(true);
    }

    public void disableCancelTrade() {
        this.cancelButton.setEnabled(false);
    }

    public void enableCancelTrade() {
        this.cancelButton.setEnabled(true);
    }

    public void disableOtherPlayerReadyMessage() {
        this.otherPlayerReadyText.setVisibility(View.GONE);
    }

    public void enableOtherPlayerReadyMessage() {
        this.otherPlayerReadyText.setVisibility(View.VISIBLE);
    }


    private void cancelButtonClicked() {
        state.cancelTradingSessionFromUI();
    }

    private void readyButtonClicked() {
        state.readyFromUI();
    }

    private void cardSelectButtonClicked() {
        // CardDiff diff;

        Intent intent = new Intent(TradeModeActivity.this, InventoryActivity.class);
        intent.putExtra("origin","TradeModeActivity"); // Show that the inventory activity is
                                                                  // started from a TradeModeActivity.

        cardSelectResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> cardSelectResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() != RESULT_OK) {
                    return;
                }
                // result.getResultCode() == RESULT_OK

                String encodedCardDiff = result.getData().getStringExtra("CardDiff");

                Gson converter = new Gson();
                CardDiff decodedCardDiff = converter.fromJson(encodedCardDiff, CardDiff.class);

                state.changeProposedCardsFromUI(new HashSet<>(Arrays.asList(decodedCardDiff)));
            }
        }
    );

    @Override
    public void onClick(View view) {
        // Change functionality depending on which button was clicked.
        switch(view.getId()) {
            case R.id.CancelButton:
                cancelButtonClicked();
                break;

            case R.id.ReadyButton:
                readyButtonClicked();
                break;

            case R.id.CardSelectButton:
                cardSelectButtonClicked();
                break;

            default:
                break;
        }
    }

    private ActivityResultLauncher<Intent> proposedCardRemoveResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() != RESULT_OK) {
                        clickedCard = null;
                        return;
                    }
                    // result.getResultCode() == RESULT_OK
                    CardDiff decodedCardDiff = new CardDiff(clickedCard, CardDiff.DiffOption.REMOVE);

                    state.changeProposedCardsFromUI(new HashSet<>(Arrays.asList(decodedCardDiff)));

                    clickedCard = null;
                }
            }
    );

    @Override
    public void OnRecyclerViewItemClick(Card clickedCard) {
        if (!state.getCardMayBeRemoved()) {
            return;
        }

        this.clickedCard = clickedCard;

        Intent intent = new Intent(TradeModeActivity.this, CardActivity.class);
        intent.putExtra("origin","TradeModeActivity"); // Show that the inventory activity is
                                                                  // started from a TradeModeActivity.
        Gson converter = new Gson();
        String encodedCard = converter.toJson(clickedCard);

        intent.putExtra("card", encodedCard);
        intent.putExtra("ShouldSupportChoosingACard", true);

        proposedCardRemoveResultLauncher.launch(intent);
    }

    @Override
    public void onBackPressed() {
        this.state.backPressedFromUI();
    }

    public void showCancelByBackPressedToast() {
        Toast toast = Toast.makeText(TradeModeActivity.this,
                                 "Cannot currently cancel",
                                      Toast.LENGTH_LONG);
        toast.show();
    }
}
