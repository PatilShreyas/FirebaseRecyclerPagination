package com.example.firebaserecyclerpagination;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView item;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        item = itemView.findViewById(R.id.textView_list);
    }

    public void setItem(String data){
        item.setText(data);
    }
}
