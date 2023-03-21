package com.example.cardhub.trading;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cardhub.Card;
import com.example.cardhub.CardSelectionFragment;
import com.example.cardhub.R;

public class TradingActivity extends AppCompatActivity {
    CardSelectionFragment f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading);

        //Management of CardSelectionFragment
        f = new CardSelectionFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView3, f, null)
                .commit();

        //Hiding and showing card selection
        Button btnSelection = findViewById(R.id.btnSelectCards);
        btnSelection.setOnClickListener(new View.OnClickListener() {
            boolean showSelection = false;
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                showSelection = !showSelection;
                    if (showSelection) {
                        fragmentManager.beginTransaction()
                                .show(f)
                                .commit();
                    }
                    else {
                        fragmentManager.beginTransaction()
                                .hide(f)
                                .commit();
                    }
                }
        });
    }
}