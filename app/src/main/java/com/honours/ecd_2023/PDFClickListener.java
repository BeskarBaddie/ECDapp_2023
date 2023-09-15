package com.honours.ecd_2023;

import android.view.View;

/**
 * Interface for handling PDF item clicks in a RecyclerView.
 */
public interface PDFClickListener {

    /**
     * Callback method triggered when a PDF item is clicked.
     *
     * @param view       The clicked view.
     * @param position   The position of the clicked item in the RecyclerView.
     * @param isLongClick `true` if it's a long click, `false` otherwise.
     */
    void onClick(View view, int position, boolean isLongClick);
}
