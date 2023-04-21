package com.example.cardhub.TradingMode;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardhub.CardRecyclerViewAdapter;
import com.example.cardhub.R;
import com.example.cardhub.authentication.LoginActivity;
import com.example.cardhub.inventory.Card;
import com.example.cardhub.inventory.InventoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class TradeModeActivity extends AppCompatActivity implements View.OnClickListener, OnRecyclerViewItemClickListener {

    private TradeModeState state; // Instance of this Trading session State
    private TradingSessionData data; // Instance of this Trading session Data

    private Button readyButton; // Button that is used to indicate that this player is ready to trade.
    private Button cancelButton; // Button that is used to indicate that this player wants to cancel
                                 // the trade.
    private Button cardSelectButton; // Button that is used to allow this player to select cards from
                                     // their inventory to propose in the trade.

    private TextView otherPlayerReadyText; // Text that is used to show that the other player is ready
                                           // to trade.

    private RecyclerView otherPlayerProposedCardsRecyclerView; // Representation of a list of cards
                                                               // proposed by the other player.
    private RecyclerView thisPlayerProposedCardsRecyclerView; // Representation of a list of cards
                                                              // proposed by this player.

    private CardRecyclerViewAdapter otherPlayerRecyclerViewAdapter; // Adapter to the RecyclerView
                                                                    // of the other player.
    private CardRecyclerViewAdapter thisPlayerRecyclerViewAdapter; // Adapter to the RecyclerView
                                                                   // of this player.

    private List<Card> otherPlayerProposedCards = new ArrayList<>(); // List of cards proposed by
                                                                     // the other player.
    private List<Card> thisPlayerProposedCards = new ArrayList<>(); // List of cards proposed by
                                                                    // this player.
    private String lid; // Lobby id
    private String clientid; // ID of this client
    private FirebaseAuth mAuth; // Entry point for the Firebase Authentication SDK.

    /**
     * Create the TradeModeActivity; inflate the view, find all relevant views, set adapters for
     * the two RecyclerViews, set this class as a listener for all buttons in the activity, initialize
     * communication with Firebase.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

        lid = getLobbyID();
        clientid = getClientID();

        TradingSessionRepository repo = new TradingSessionRepositoryImpl();
        data = new TradingSessionData(repo, lid, clientid);
        repo.setData(data);
        state = new TradeModeState(this, repo, clientid);
        repo.setReceiver(state);
        mAuth = FirebaseAuth.getInstance();

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
     * Get the ID of the client that instantiated this TradeModeActivity.
     * @return the ID of the client that instantiated this TradeModeActivity.
     */
    private String getClientID() {
        return getIntent().getStringExtra("clientid");
    }

    /**
     * Get the lobby ID that was passed when starting this TradeModeActivity.
     * @return the lobby ID that was passed when starting this TradeModeActivity.
     */
    private String getLobbyID() { return getIntent().getStringExtra("lobbyid"); }

    /**
     * Update the UI in activity using the data from the TradingSession instance.
     * @param tradingSession the TradingSession instance that stores which cards are proposed by both
     *                       players in the trade.
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

    /**
     * Cancel the Trading Session.
     */
    public void cancelTradeMode() {
        finish();
    }

    /**
     * Disallow this player to change their proposed trade.
     */
    public void disableChangeTradeProposal() {
        this.cardSelectButton.setEnabled(false);
    }

    /**
     * Allow this player to change their proposed trade.
     */
    public void enableChangeTradeProposal() {
        this.cardSelectButton.setEnabled(true);
    }

    /**
     * Disallow this player to accept the currently proposed trade.
     */
    public void disableAcceptTrade() {
        this.readyButton.setEnabled(false);
    }

    /**
     * Allow this player to accept the currently proposed trade.
     */
    public void enableAcceptTrade() {
        this.readyButton.setEnabled(true);
    }

    /**
     * Disallow this player to cancel the trading session.
     */
    public void disableCancelTrade() {
        this.cancelButton.setEnabled(false);
    }

    /**
     * Allow this player to cancel the trading session.
     */
    public void enableCancelTrade() {
        this.cancelButton.setEnabled(true);
    }

    /**
     * Hide the message indicating that the other player is ready to trade.
     */
    public void disableOtherPlayerReadyMessage() {
        this.otherPlayerReadyText.setVisibility(View.GONE);
    }

    /**
     * Show the message indicating that the other player is ready to trade.
     */
    public void enableOtherPlayerReadyMessage() {
        this.otherPlayerReadyText.setVisibility(View.VISIBLE);
    }

    /**
     * Handle the event where the cancel button is clicked from the current state.
     */
    private void cancelButtonClicked() {
        state.cancelTradingSessionFromUI();
    }

    /**
     * Handle the event where the ready button is clicked from the current state.
     */
    private void readyButtonClicked() {
        state.readyFromUI();
    }

    /**
     * Handle the event where the card select button is clicked from the current state.
     */
    private void cardSelectButtonClicked() {
        state.cardSelectFromUI();
    }

    /**
     * Handle the event when one of the views that this activity is listening to was clicked.
     * @param view The view that was clicked.
     */
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

    /**
     * Handle the event when one of the cards proposed by this player was clicked.
     * @param clickedCard Card in the RecyclerView that was clicked.
     */
    @Override
    public void OnRecyclerViewItemClick(Card clickedCard) {
        state.proposedCardClickedFromUI(clickedCard);
    }

    /**
     * Handle the event when the back button gets pressed.
     */
    @Override
    public void onBackPressed() {
        state.backPressedFromUI();
    }

    /**
     * Show a toast notifying the user that the trading session cannot currently be canceled.
     */
    public void showCancelByBackPressedToast() {
        Toast toast = Toast.makeText(TradeModeActivity.this,
                                 "Cannot currently cancel",
                                      Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Finish the trading session.
     */
    public void finishTrade() {
        Intent finishTradeIntent = new Intent(this, InventoryActivity.class);
        startActivity(finishTradeIntent);
    }

    /**
     * Start the activity according to whether current player is logged in correctly.
     */
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
