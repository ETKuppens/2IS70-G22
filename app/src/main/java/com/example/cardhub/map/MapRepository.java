package com.example.cardhub.map;

import java.util.List;
import java.util.Map;

public interface MapRepository {
    public void requestPacks();

    void receivePacks(List<Map<String, Object>> packsRaw);
}
