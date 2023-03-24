package com.example.cardhub;

import com.example.cardhub.inventory.InventoryRepositoryReceiver;

import java.util.Set;

/**
 * Class implementing the TradingSessionRepository interface
 */
public class TradingSessionRepositoryImpl implements TradingSessionRepository {
    InventoryRepositoryReceiver receiver;

    @Override
    public void cancelTradingSession(int clientID){
    }

    @Override
    public void cancelTradingSessionConfirm(int clientID) {

    }

    @Override
    public void acceptProposedTrade(int clientID) {

    }

    @Override
    public void cancelAcceptTrade(int clientID) {

    }

    @Override
    public void changeProposedCards(int clientID, Set<CardDiff> diffs) {

    }

    @Override
    public void changeProposedCardsConfirm(int clientID) {

    }
}
