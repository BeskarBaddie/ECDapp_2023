package com.honours.ecd_2023;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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


    }
}
