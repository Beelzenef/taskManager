package com.example.taskmng.adapter;

import android.view.View;

/**
 * Created by paco on 6/02/18.
 */

public interface ClickListener{
    public void onClick(View view, int position);
    public void onLongClick(View view, int position);
}