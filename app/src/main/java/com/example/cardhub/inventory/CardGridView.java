package com.example.cardhub.inventory;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * GridView that handles updating the dataset correctly
 */
public class CardGridView extends GridView {

    boolean expanded = false;

    private static final String TAG = CardGridView.class.getName();

    /**
     * Constructs a new instance of the CardGridView class with the specified Context.
     * @param context the context in which this view will be displayed
     */
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

    /**
     * An implementation of DataSetObserver that receives callbacks when the underlying data
     * set for an adapter has changed.
     */
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

    /**
     * Sets the adapter that provides data to this view.
     * @param adapter The adapter to set.
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;

        mAdapter.registerDataSetObserver(mDataSetObserver);
    }

    /**
     * Refreshes the visible views by calling getView() on the adapter for each visible child.
     * If the adapter is null or any visible view is null, no action is taken for that child.
     */
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

    /**
     * Returns whether this item is currently expanded or collapsed.
     * @return true if this item is expanded, false otherwise.
     */
    public boolean isExpanded()
    {
        return expanded;
    }

    /**
     * Measures the view and sets its size based on the measurement parameters.
     * Overrides the default behavior to expand the view's height if it is currently expanded.
     * If it is not expanded, the view's height is set according to the heightMeasureSpec parameter.
     * @param widthMeasureSpec The width measurement specification.
     * @param heightMeasureSpec The height measurement specification.
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded())
        {
            // Calculate entire height by providing a very large height hint.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * Sets the expanded state of this item.
     * @param expanded true if this item should be expanded, false if it should be collapsed.
     */
    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }
}
