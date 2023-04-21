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

    /**
     * This is the constructor for the CardGridAdapter class.
     * It initializes the adapter with a given context and list of Card objects.
     * @param context the context of the adapter
     * @param cards a list of Card objects to be displayed in the adapter
     */
    public CardGridAdapter(Context context, List<Card> cards) {
        this.context = context;
        this.cards = cards;
        inflater = LayoutInflater.from(context);
    }

    /**
     * This method updates the data in the CardGridAdapter with a new list of Card objects.
     * If the new list is not the same as the old list, the method clears the old list and adds
     * all the new Cards.
     * @param newCards the new list of Card objects to be displayed in the adapter
     */
    public void updateData(List<Card> newCards) {
        if (newCards != cards) {
            cards.clear();
            cards.addAll(newCards);
        }
    }

    /**
     * This method returns the number of Card objects in the CardGridAdapter.
     * @return the number of Card objects in the adapter
     */
    @Override
    public int getCount() {
        return cards.size();
    }

    /**
     * This method returns the Card object at the specified position in the CardGridAdapter.
     * @param i the position of the Card object to return
     * @return the Card object at the specified position
     */
    @Override
    public Object getItem(int i) {
        return cards.get(i);
    }

    /**
     * This method returns the ID of the Card object at the specified position
     * in the CardGridAdapter.
     * @param i the position of the Card object whose ID is to be returned
     * @return the ID of the Card object at the specified position
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * This method returns a View that displays the Card object at the specified position
     * in the CardGridAdapter.
     * @param i the position of the Card object to display
     * @param view the old view to reuse, if possible
     * @param viewGroup the parent view that the returned view will be attached to
     * @return a View that displays the Card object at the specified position
     */
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
