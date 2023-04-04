package com.example.cardhub.TradingMode;

import java.util.Set;

/**
 * Class implementing the TradingSessionRepository interface
 */
public class TradingSessionRepositoryImpl implements TradingSessionRepository {
    TradingSessionData data;
    TradingSessionRepositoryReceiver receiver;

    public TradingSessionRepositoryImpl (TradingSessionRepositoryReceiver receiver, String lid, String clientid) {
        data = new TradingSessionData(this, lid, clientid);
        this.receiver = receiver;
    }

    @Override
    public void cancelTradingSession(String clientID){
        data.cancelTradingSession(clientID);
    }

    @Override
    public void cancelTradingSessionConfirm(String clientID) {

    }

    @Override
    public void acceptProposedTrade(String clientID) {
        data.acceptProposedTrade(clientID);
    }

    @Override
    public void cancelAcceptTrade(String clientID) {
        data.cancelAcceptTrade(clientID);
    }

    @Override
    public void changeProposedCards(String clientID, Set<CardDiff> diffs) {
        data.changeProposedCards(clientID, diffs);
    }

    @Override
    public void changeProposedCardsConfirm(String clientID) {
        receiver.changeProposedCardsResponse();
    }
}
