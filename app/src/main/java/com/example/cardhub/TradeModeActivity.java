package com.example.cardhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TradeModeActivity extends AppCompatActivity implements View.OnClickListener {

    private TradeModeState state;

    private Button readyButton;
    private Button cancelButton;
    private Button cardSelectButton;

    private TextView otherPlayerReadyText;

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
    }

    /**
     * Update the UI in activity using the data from the TradingSession instance.
     * TODO: implement this method
     */
    public void updateUI(TradingSession tradingSession) {}

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

        // state.changeProposedCardsFromUI(diff);
    }

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
