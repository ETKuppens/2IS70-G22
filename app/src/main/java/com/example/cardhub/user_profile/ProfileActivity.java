package com.example.cardhub.user_profile;

import android.content.Intent;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardhub.CardRecyclerViewAdapter;
import com.example.cardhub.inventory.Card;
import com.example.cardhub.map.CardPack;
import com.example.cardhub.map.MapActivity;
import com.example.cardhub.PairingModeActivity;
import com.example.cardhub.collector_navigation.CollectorBaseActivity;
import com.example.cardhub.authentification.LoginActivity;
import com.example.cardhub.R;
import com.example.cardhub.inventory.InventoryActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends CollectorBaseActivity {
    ProfileState state;

    PopupWindow cardpackPreviewWindow = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    public int getBottomNavigationMenuItemId() {
        return R.id.action_profile;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.state = new ProfileState();
        updateData();

        setupNav();

        //Bind functionality to Buttons
        Button logoutButton = (Button)findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logout);
                FirebaseAuth.getInstance().signOut();
            }
        });

        Button cardpackPreviewTestButton = (Button)findViewById(R.id.test_cardpack_preview_button);
        cardpackPreviewTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardpackPreviewWindow != null) {
                    return;
                }

                List<Card> cardList = new ArrayList<Card>();

                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                View cardpackPreviewView = inflater.inflate(R.layout.cardpack_preview, null);

                cardpackPreviewWindow = new PopupWindow(cardpackPreviewView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                ImageButton cardpackPreviewCloseButton = cardpackPreviewView
                        .findViewById(R.id.cardpack_preview_close_button);

                cardpackPreviewCloseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        destroyCardpackPreviewWindow();
                    }
                });

                RecyclerView cardpackRecyclerView = cardpackPreviewView
                        .findViewById(R.id.cardpack_preview_cards_recyclerview);
                CardRecyclerViewAdapter adapter = new CardRecyclerViewAdapter(getApplicationContext(),
                                                                              cardList);
                cardpackRecyclerView.setAdapter(adapter);

                cardpackPreviewWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_inventory) {
            startActivity(new Intent(this, InventoryActivity.class));
        } else if (itemId == R.id.action_map) {
            startActivity(new Intent(this, MapActivity.class));
        } else if (itemId == R.id.action_trading) {
            startActivity(new Intent(this, PairingModeActivity.class));
        } else if (itemId == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
        return true;
    }

    void updateData() {
        ImageView profilePic = findViewById(R.id.profilePicture);
        profilePic = state.getProfilePicture();

        TextView userName = findViewById(R.id.userName);
        userName.setText(state.getUsername());

        TextView cardAmount = findViewById(R.id.cardsCollectedAmount);
        cardAmount.setText(state.getCardAmount());

        TextView tradeAmount = findViewById(R.id.tradeAmount);
        tradeAmount.setText(state.getTradeAmount());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        destroyCardpackPreviewWindow();
    }

    private void destroyCardpackPreviewWindow() {
        cardpackPreviewWindow.dismiss();
        cardpackPreviewWindow = null;
    }
}