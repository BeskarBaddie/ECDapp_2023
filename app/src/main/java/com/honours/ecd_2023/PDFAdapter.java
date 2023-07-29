package com.honours.ecd_2023;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;

public class PDFAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    public PDFClickListener pdfClickListener;
    private final Context context;
    public TextView pdfTitle;

    public PDFAdapter(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        pdfTitle = itemView.findViewById(R.id.pdf_name);
    }

    @Override
    public void onClick(View v) {

        pdfClickListener.onClick(v, getAdapterPosition(),false);
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_ID, "pdf_item_" + getAdapterPosition());
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, pdfTitle.getText().toString());
        params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "pdf");
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);

    }
}
