package com.example.cardhub.TradingMode;

import com.example.cardhub.inventory.Card;

/**
 * Interface for classes that listen to clicks on Cards that are represented by a RecyclerView.
 */
public interface OnRecyclerViewItemClickListener {
    /**
     * Method that gets called whenever a Card gets clicked in this RecyclerView.
     * @param clickedView Card in the RecyclerView that was clicked.
     */
    void OnRecyclerViewItemClick(Card clickedView);
}
