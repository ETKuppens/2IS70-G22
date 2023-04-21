package com.example.cardhub.map;

import java.util.List;

public interface MapRepositoryReceiver {

    /**
     * Sets packs from the database
     *
     * @param packs List of packs from the database
     */
    void setPacks(List<CardPack> packs);
}
