package com.dicoding.mynotesapp;

import android.view.View;

// fungsi class bertugas membuat item seperti CardView bisa di klik dalam adapter
public class CustomOnItemClickListener implements View.OnClickListener {

    private int positon;
    private OnItemClickCallback onItemClickCallback;

    // construct
    public CustomOnItemClickListener(int positon, OnItemClickCallback onItemClickCallback) {
        this.positon = positon;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onClick(View v) {
        onItemClickCallback.onItemClicked(v, positon);
    }

    public interface OnItemClickCallback {
        void onItemClicked(View view, int position);
    }
}
