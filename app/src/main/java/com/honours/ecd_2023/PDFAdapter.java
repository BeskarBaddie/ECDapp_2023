package com.honours.ecd_2023;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Custom RecyclerView adapter for displaying PDF items.
 */
public class PDFAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    public PDFClickListener pdfClickListener;
    private final Context context;
    public TextView pdfTitle;

    /**
     * Constructor to initialize a PDFAdapter instance.
     *
     * @param itemView The root view of the PDF item layout.
     */
    public PDFAdapter(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        pdfTitle = itemView.findViewById(R.id.pdf_name);
    }

    /**
     * Handles the click event on a PDF item.
     *
     * @param v The clicked view.
     */
    @Override
    public void onClick(View v) {
        pdfClickListener.onClick(v, getAdapterPosition(), false);

        // Log a Firebase Analytics event for the selected PDF item.
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, pdfTitle.getText().toString());
        params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "pdf");
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);
    }
}
