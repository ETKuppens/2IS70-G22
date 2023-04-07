package com.example.cardhub.inventory;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.cardhub.R;

import java.util.List;

public class CardGridAdapter extends BaseAdapter {
    Context context;
    List<Card> cards;

    LayoutInflater inflater;

    public CardGridAdapter(Context context, List<Card> cards) {
        this.context = context;
        this.cards = cards;
        inflater = LayoutInflater.from(context);
    }

    public void updateData(List<Card> newCards) {
        if (newCards != cards) {
            cards.clear();
            cards.addAll(newCards);
        }
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int i) {
        return cards.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.card_grid_item, viewGroup, false);
        }

        Card thisCard = cards.get(i);

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
        if (!thisCard.acquired)  {
            cardImage.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            cardImage.clearColorFilter();
        }
        Glide.with(context).load(thisCard.IMAGE_URL).into(cardImage);

        return view;
    }
}
