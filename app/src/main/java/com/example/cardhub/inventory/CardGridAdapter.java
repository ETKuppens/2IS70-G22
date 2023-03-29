package com.example.cardhub.inventory;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.example.cardhub.R;

import java.io.InputStream;
import java.net.URL;
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

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.card_grid_item, null);
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

            case UNKNOWN:
                color = ContextCompat.getColor(context, R.color.rarity_unknown);
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
