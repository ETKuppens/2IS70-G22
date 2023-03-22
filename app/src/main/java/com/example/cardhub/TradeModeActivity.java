package com.example.cardhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TradeModeActivity extends AppCompatActivity implements View.OnClickListener {

    TradeModeState state;

    Button readyButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_mode);

        readyButton = findViewById(R.id.ReadyButton);
        cancelButton = findViewById(R.id.CancelButton);

        readyButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
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

    }

    public void enableChangeTradeProposal() {

    }


    private void cancelButtonClicked() {

    }

    private void readyButtonClicked() {

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

            default:
                break;
        }
    }
}
