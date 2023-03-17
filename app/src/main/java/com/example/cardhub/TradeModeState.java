package com.example.cardhub;

public class TradeModeState {
    TradeModeActivity activity;

    // TradingSession instance that keeps track which user proposes which cards in the current
    // trading session.
    TradingSession tradingSession = new TradingSession();

    /**
     * Construct a new TradeModeState that is linked to an existing TradeModeActivity.
     * @param activity the TradeModeActivity storing the UI that should be represented by this TradeModeState.
     */
    TradeModeState(TradeModeActivity activity) {
        this.activity = activity;
    }

    /**
     * Update the UI in activity using the data from the TradingSession instance.
     */
    private void updateUI() {
        activity.updateUI(tradingSession);
    }
}
