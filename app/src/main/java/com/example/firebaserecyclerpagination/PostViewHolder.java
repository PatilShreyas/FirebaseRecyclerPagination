package com.example.firebaserecyclerpagination;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class PostViewHolder extends RecyclerView.ViewHolder {
    TextView textViewTitle;
    TextView textViewBody;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewBody = itemView.findViewById(R.id.textViewBody);
    }

    public void setItem(Post post){
        textViewTitle.setText(post.title);
        textViewBody.setText(post.body);
    }
}
