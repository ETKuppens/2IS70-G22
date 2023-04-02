package com.example.cardhub;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardhub.TradingMode.OnRecyclerViewItemClickListener;
import com.example.cardhub.inventory.Card;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardRecyclerViewAdapter.CardViewHolder> {
    private List<Card> representedCards; // List of cards that get represented by the RecyclerView.

    private Context context;

    LayoutInflater inflater;

    private OnRecyclerViewItemClickListener listener = null;

    public CardRecyclerViewAdapter(Context context, List<Card> representedCards) {
        // Set the list of cards that should be represented by the RecyclerView to the passed list
        // of cards.
        this.representedCards = representedCards;

        this.context = context;

        inflater = LayoutInflater.from(context);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardRecyclerViewAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = setView(0, null);
        CardViewHolder viewHolder = new CardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardRecyclerViewAdapter.CardViewHolder holder, int position) {
        setView(position, holder.cardView);
    }

    @Override
    public int getItemCount() {
        return representedCards.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View cardView;

        public CardViewHolder(@NonNull View view) {
            super(view);

            // TODO: check if this is correct
            cardView = view;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.OnRecyclerViewItemClick(representedCards.get(getAdapterPosition()));
            }
        }
    }

    private View setView(int i, View view) {
        if (view == null) {
            view = inflater.inflate(R.layout.card_grid_item, null);
        }

        Card thisCard = representedCards.get(i);

        View cardBackground = view.findViewById(R.id.card_background);
        int color = 0;
        switch (thisCard.RARITY) {
            case COMMON:
                color = ContextCompat.getColor(context, R.color.rarity_common);
                break;

            case LEGENDARY:
                color = ContextCompat.getColor(context, R.color.rarity_legendary);
                break;

            case RARE:
                color = ContextCompat.getColor(context, R.color.rarity_rare);
                break;

            case ULTRA_RARE:
                color = ContextCompat.getColor(context, R.color.rarity_ultra_rare);
                break;
        }
        cardBackground.setBackgroundColor(color);


        ImageView cardImage = view.findViewById(R.id.card_image);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = (InputStream) new URL(thisCard.IMAGE_URL).getContent();
                    Drawable d = Drawable.createFromStream(is, "src name");
                    cardImage.setImageDrawable(d);
                } catch (Exception e) {
                    Log.d("CARDGRID", e.toString());
                }
            }
        });

        thread.start();

        return view;
    }
}
