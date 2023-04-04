package com.example.cardhub.TradingMode;

import com.example.cardhub.inventory.Card;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class implementing the TradingSessionRepository interface
 */
public class TradingSessionRepositoryImpl implements TradingSessionRepository {
    TradingSessionData data;
    TradingSessionRepositoryReceiver receiver;
    TradingSession tradingSession;

    public TradingSessionRepositoryImpl (TradingSessionRepositoryReceiver receiver, String lid, String clientid, TradingSession tradingSession) {
        data = new TradingSessionData(this, lid, clientid);
        this.receiver = receiver;
        this.tradingSession = tradingSession;
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
        Set<Card> cards = tradingSession.thisUserProposedCards;
        data.acceptProposedTrade(clientID, cards);
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

    @Override
    public void receiveUpdate(List<Map<String, Object>> diffs) {
        Set<CardDiff> diffs_list = diffs.stream().map((dif) -> {
            Map<String, Object> card = (Map<String, Object>) dif.get("card");
            CardDiff newDif = new CardDiff(
                new Card(
                        (String) card.get("NAME"),
                        (String) card.get("DESCRIPTION"),
                        Card.Rarity.valueOf((String) card.get("RARITY")),
                        (String) card.get("IMAGE_URL"),
                        (String) card.get("CARD_ID")
                ),
                CardDiff.DiffOption.valueOf((String)dif.get("diff"))
                );
            return newDif;
        }).collect(Collectors.toSet());

        receiver.changeProposedCards(diffs_list);

        // TODO: Delete card diffs
    }
}
