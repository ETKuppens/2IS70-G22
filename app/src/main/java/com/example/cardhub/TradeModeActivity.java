package com.example.cardhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TradeModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_mode);
    }

    /**
     * Update the UI in activity using the data from the TradingSession instance.
     * TODO: implement this method
     */
    public void updateUI(TradingSession tradingSession) {}

    public void cancelTradeMode() {
        finish();
    }
}