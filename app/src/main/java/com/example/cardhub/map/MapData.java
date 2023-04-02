package com.example.cardhub.map;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.AttachedSurfaceControl;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapData {
    MapRepository receiver;
    FirebaseFirestore db;

    public MapData(MapRepository receiver) {
        this.receiver = receiver;
        this.db = FirebaseFirestore.getInstance();
    }

    public void requestPacks() {
        db.collection("cardpacks/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Map<String, Object>> packs = task.getResult().getDocuments().stream()
                            .map((DocumentSnapshot snapshot) -> snapshot.getData())
                            .collect(Collectors.toList());


                    receiver.receivePacks(packs);


                } else {

                }
            }
        });
    }
}
