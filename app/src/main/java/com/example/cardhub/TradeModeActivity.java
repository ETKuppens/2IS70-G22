package com.example.cardhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TradeModeActivity extends AppCompatActivity {

    // TradingSession instance that keeps track which user proposes which cards in the current
    // trading session.
    TradingSession tradingSession = new TradingSession();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_mode);
    }

    /**
     * Update the UI using the data from the TradingSession instance.
     * TODO: implement this method
     */
    private void updateUI() {}
}