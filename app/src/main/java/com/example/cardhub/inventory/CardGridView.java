package com.example.cardhub.inventory;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * GridView that handles updating the dataset correctly
 */
public class CardGridView extends GridView {

    private static final String TAG = CardGridView.class.getName();

    @SuppressWarnings("unused")
    public CardGridView(Context context) {
        super(context);
    }

    @SuppressWarnings("unused")
    public CardGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("unused")
    public CardGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();

            refreshVisibleViews();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();

            refreshVisibleViews();
        }
    }

    private DataSetObserver mDataSetObserver = new AdapterDataSetObserver();
    private ListAdapter mAdapter;

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;

        mAdapter.registerDataSetObserver(mDataSetObserver);
    }

    void refreshVisibleViews() {
        if (mAdapter != null) {
            for (int i = getFirstVisiblePosition(); i <= getLastVisiblePosition(); i ++) {
                final int dataPosition = i - getFirstVisiblePosition();// - getChildCount();
                final int childPosition = i - getFirstVisiblePosition();

                if (dataPosition >= 0 && dataPosition < mAdapter.getCount()
                        && getChildAt(childPosition) != null) {
                    mAdapter.getView(dataPosition, getChildAt(childPosition), this);
                }
            }
        }
    }
}
