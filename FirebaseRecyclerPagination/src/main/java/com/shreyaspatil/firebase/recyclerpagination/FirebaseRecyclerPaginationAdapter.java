package com.shreyaspatil.firebase.recyclerpagination;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;



public abstract class FirebaseRecyclerPaginationAdapter<T,H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H>{

    private Context mContext;
    private ArrayList<T> mList;

    public FirebaseRecyclerPaginationAdapter(Context mContext){
        this.mContext = mContext;
    }

    void setList(ArrayList<T> mList){
        this.mList = mList;
    }

    @Override
    public void onBindViewHolder(@NonNull H viewHolder, int position) {
        T model = mList.get(position);
        onBindViewHolder(viewHolder, position, model);
    }

    protected abstract void onBindViewHolder(@NonNull H viewHolder, int position, @NotNull T model);

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
