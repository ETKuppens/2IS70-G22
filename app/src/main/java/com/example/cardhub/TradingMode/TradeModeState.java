package com.example.cardhub.TradingMode;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.cardhub.inventory.Card;
import com.example.cardhub.inventory.CardActivity;
import com.example.cardhub.inventory.InventoryActivity;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TradeModeState implements TradingSessionRepositoryReceiver {
    private String clientid;

    private TradeModeActivity activity; // Activity this state is interacting with.
    private TradingSessionRepository repository; // Repository this state is interacting with.

    // TradingSession instance that keeps track which user proposes which cards in the current
    // trading session.
    private TradingSession tradingSession = new TradingSession();

    private Card clickedCard = null; // Card that was clicked to be removed

    /**
     * Construct a new TradeModeState that is linked to an existing TradeModeActivity.
     *
     * @param activity the TradeModeActivity storing the UI that should be represented by this TradeModeState.
     * @param clientid ID of the client that instantiated this TradeModeState.
     */
    public TradeModeState(TradeModeActivity activity, TradingSessionRepository repo, String clientid) {
        this.activity = activity;
        this.clientid = clientid;
        this.repository = repo;

        this.cardSelectResultLauncher = activity.registerForActivityResult(
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

                        changeProposedCardsFromUI(new HashSet<>(Arrays.asList(decodedCardDiff)));
                    }
                }
        );

        this.proposedCardRemoveResultLauncher = activity.registerForActivityResult(
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

                        changeProposedCardsFromUI(new HashSet<>(Arrays.asList(decodedCardDiff)));

                        clickedCard = null;
                    }
                }
        );
    }

    // List of flags that are used to check when certain functionality can be called.
    /**
     * Whether the cards that this user proposes in the trade may be changed. Must be false when
     * this app instance is currently waiting for a response from the server stating that a previous
     * proposed cards changed was handled correctly.
     */
    private boolean proposedCardsMayBeChanged = true;
    /**
     * Whether the current trading proposal may be accepted by this app instance. May be false when
     * this app instance is currently canceling the trading session, or when this app instance is
     * already proposing the trade to be accepted.
     */
    private boolean proposedTradeMayBeAccepted = true;
    /**
     * Whether the current trading proposal may be canceled by this app instance. May be false when
     * this app instance is currently trying to accept the trading proposal.
     */
    private boolean proposedTradeMayBeCanceled = true;

    /**
     * Handle the event when proposed cards are changed from the UI.
     * @param diffs set of CardDiffs that show which cards should be added and which should be
     *              removed.
     */
    public void changeProposedCardsFromUI(Set<CardDiff> diffs) {
        if (!this.proposedCardsMayBeChanged)
        {
            throw new RuntimeException("TradeModeState.changeProposedCards: the trade session is" +
                    "currently in a state where the proposed cards may not be changed.");
        }

        this.proposedCardsMayBeChanged = false;
        this.activity.disableChangeTradeProposal();

        this.tradingSession.AddCardDiffsForThisUser(diffs);
        updateUI();

        this.repository.changeProposedCards(this.clientid, diffs);
    }

    /**
     * Perform the steps needed to cancel the trading session;
     * 1. Make sure that the proposed cards cannot be changed anymore
     * 2. Ask the server to also cancel the trading mode of the other client instance in this
     * trading session
     * 3. Cancel the trading session of this client instance.
     */
    public void cancelTradingSessionFromUI() {
        if (!this.proposedTradeMayBeCanceled) {
            throw new RuntimeException("TradeModeState.cancelTradingSessionFromUI: the trade " +
                    "session is" + "currently in a state where the proposed trade may not be " +
                    "canceled.");
        }

        this.proposedCardsMayBeChanged = false;
        this.activity.disableChangeTradeProposal();

        this.proposedTradeMayBeAccepted = false;
        this.activity.disableAcceptTrade();

        this.proposedTradeMayBeCanceled = false;
        this.activity.disableCancelTrade();

        this.repository.cancelAcceptTrade(this.clientid);
        cancelTradeMode();

    }

    /**
     * Receive a message that the back button is pressed from the UI.
     */
    public void backPressedFromUI() {
        if (this.proposedTradeMayBeCanceled) {
            this.cancelTradingSessionFromUI();
        } else { // !this.proposedTradeMayBeCanceled
            this.activity.showCancelByBackPressedToast();
        }
    }

    /**
     * Receive a message that the ready button was clicked in the UI.
     */
    public void readyFromUI() {
        acceptTrade();
    }

    /**
     * Receive a message that the card select button was clicked in the UI.
     */
    public void cardSelectFromUI() {
        Intent intent = new Intent(this.activity, InventoryActivity.class);
        intent.putExtra("origin","TradeModeActivity"); // Show that the inventory activity is
                                                                  // started from a TradeModeActivity.

        cardSelectResultLauncher.launch(intent);
    }

    /**
     * Intent launcher used to create a new activity to select cards from the players inventory
     * to propose in the trade.
     */
    private ActivityResultLauncher<Intent> cardSelectResultLauncher;

    /**
     * Receive a message that one of the cards proposed by this player was clicked in the UI.
     * @param clickedCard the proposed card that was clicked.
     * @throws NullPointerException when {@code clickedCard == null}
     */
    public void proposedCardClickedFromUI(Card clickedCard) {
        if (clickedCard == null) {
            throw new NullPointerException("TradeModeState.proposedCardClickedFromUI: " +
                    "argument 'clickedCard' may not be null.");
        }

        if (!this.getCardMayBeRemoved()) {
            return;
        }

        this.clickedCard = clickedCard;

        Intent intent = new Intent(activity, CardActivity.class);
        intent.putExtra("origin","TradeModeActivity"); // Show that the inventory activity is
                                                                  // started from a TradeModeActivity.
        Gson converter = new Gson();
        String encodedCard = converter.toJson(clickedCard);

        intent.putExtra("card", encodedCard);
        intent.putExtra("ShouldSupportChoosingACard", true);

        proposedCardRemoveResultLauncher.launch(intent);
    }

    /**
     * Intent launcher used to create a new activity to remove a card from this players proposed
     * trade.
     */
    private ActivityResultLauncher<Intent> proposedCardRemoveResultLauncher;

    /**
     * Update the UI in activity using the data from the TradingSession instance.
     */
    private void updateUI() {
        activity.updateUI(tradingSession);
    }

    private void cancelTradeMode() {
        this.proposedTradeMayBeAccepted = false;
        this.activity.disableAcceptTrade();

        this.activity.cancelTradeMode();
    }

    /**
     * Apply the proposed cards to the inventory of this user, and exit this trading session.
     */
    private void acceptTrade() {
        if (!this.proposedTradeMayBeAccepted) {
            throw new RuntimeException("TradeModeState.acceptTrade: the trade session is" +
                    "currently in a state where the proposed trade may not be accepted.");
        }

        this.proposedTradeMayBeAccepted = false;
        this.activity.disableAcceptTrade();

        this.repository.acceptProposedTrade(this.clientid);
    }


    /**
     * Cancel the trading session.
     */
    @Override
    public void cancelTradingSession() {
        this.proposedCardsMayBeChanged = false;
        this.activity.disableChangeTradeProposal();

        this.proposedTradeMayBeAccepted = false;
        this.activity.disableAcceptTrade();

        this.repository.cancelTradingSessionConfirm(this.clientid);
        cancelTradeMode();
    }


    /**
     * Receive a message from the server that the other party has canceled the Trading Session.
     */
    @Override
    public void cancelTradingSessionResponse() {
        cancelTradeMode();
    }


    /**
     * Receive a message from the server whether the other party has accepted the trade proposal.
     * @param tradeAccepted whether the trade was accepted by the other client instance.
     */
    @Override
    public void acceptProposedTradeResponse(boolean tradeAccepted) {
        if (tradeAccepted) {
            acceptTrade();
        }
        else { // !tradeAccepted
            this.proposedCardsMayBeChanged = true;
            this.activity.enableChangeTradeProposal();

            this.proposedTradeMayBeAccepted = true;
            this.activity.enableAcceptTrade();

            this.proposedTradeMayBeCanceled = true;
            this.activity.enableCancelTrade();

            this.repository.cancelAcceptTrade(this.clientid);
        }
    }

    /**
     * Receive a message from the server that the other player is ready to accept the trade.
     */
    @Override
    public void acceptProposedTradeFromOtherTrader() {
        this.activity.enableOtherPlayerReadyMessage();
    }

    /**
     * Receive a message from the server that the other player has changed their proposed cards.
     * @param diffs a set of CardDiffs that should be applied to the other clients' proposed cards.
     */
    @Override
    public void changeProposedCards(Set<CardDiff> diffs) {
        this.tradingSession.AddCardDiffsForOtherUser(diffs);
        updateUI();
        this.repository.changeProposedCardsConfirm(this.clientid);
    }

    /**
     * Receive a message from the server that the other player has received the change in proposed
     * cards correctly.
     */
    @Override
    public void changeProposedCardsResponse() {
        this.proposedCardsMayBeChanged = true;
        this.activity.enableChangeTradeProposal();
    }

    // ADD ABILITY TO CANCEL TRADE
    @Override
    public void startTradeTimer() {
        new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long countDownVal = millisUntilFinished / 1000;
                Log.d("TIMER", countDownVal+ " seconds left");
            }

            @Override
            public void onFinish() {
                repository.doTrade();
            }
        }.start();
    }

    /**
     * Finish the trade from the Activity.
     */
    @Override
    public void finishTrade() {
        activity.finishTrade();
    }

    /**
     * Get whether a proposed card may currently be removed.
     * @return this.proposedCardsMayBeChanged.
     */
    public boolean getCardMayBeRemoved() {
        if (this.proposedCardsMayBeChanged) {
            return true;
        }

        Toast toast = Toast.makeText(activity,
                "Cannot currently change proposed cards",
                Toast.LENGTH_LONG);
        toast.show();

        return false;
    }
}
