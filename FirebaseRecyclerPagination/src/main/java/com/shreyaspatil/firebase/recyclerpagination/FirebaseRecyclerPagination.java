package com.shreyaspatil.firebase.recyclerpagination;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.shreyaspatil.firebase.recyclerpagination.listener.FirebaseRecyclerPaginationListener;

import java.util.ArrayList;

public class FirebaseRecyclerPagination<T> {

    private Context mContext;

    private Query mQuery;

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerPaginationAdapter mAdapter;
    private LinearLayoutManager mManager;

    private FirebaseRecyclerPaginationListener mListener;

    private int mCurrentItems;
    private int mTotalItemCount;
    private int mTotalScrollOut;
    private int initialItems;
    private int nextItems;
    private String mLastKey;

    public static final int ORDER_ASCENDING = 1;
    public static final int ORDER_DESCENDING = 2;
    private int mOrder;

    private boolean isScrolling;
    private boolean isLoading;

    private Class<T> tClass;
    private ArrayList<T> mList;

    @IntDef({
            ORDER_ASCENDING,
            ORDER_DESCENDING
    })
    public @interface Order { }

    public FirebaseRecyclerPagination(Context mContext, RecyclerView mRecyclerView, Class<T> tClass, Query mQuery, @Order int order, int initialItems, int nextItems){
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
        this.initialItems = initialItems;
        this.nextItems = nextItems;
        this.tClass = tClass;
        this.mQuery = mQuery;
        this.mOrder = order;

        mManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mCurrentItems = mManager.getChildCount();
                mTotalItemCount = mManager.getItemCount();
                mTotalScrollOut = mManager.findFirstVisibleItemPosition();

                if(isScrolling && !isLoading && (mCurrentItems+mTotalScrollOut) == mTotalItemCount){
                    isScrolling = false;
                    isLoading = true;

                    if(mListener != null)
                        mListener.onLoading();

                    loadNext();
                }
            }
        });
    }

    public void setPaginationAdapter(FirebaseRecyclerPaginationAdapter mAdapter){
        this.mAdapter = mAdapter;

        mList = new ArrayList<T>();
        mAdapter.setList(mList);

        mRecyclerView.setAdapter(this.mAdapter);
        mAdapter.notifyDataSetChanged();

        init();
    }

    public void setFirebaseRecyclerPaginationListener(@NotNull FirebaseRecyclerPaginationListener mListener){
        this.mListener = mListener;
    }

    private void init(){

        if(mListener!=null)
            mListener.onLoading();


        Query mInitQuery;
        if(mOrder == ORDER_ASCENDING)
            mInitQuery = mQuery.limitToFirst(initialItems);
        else
            mInitQuery = mQuery.limitToLast(initialItems);

        mInitQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    T data = ds.getValue(tClass);
                    mLastKey = ds.getKey();
                    mList.add(data);
                }
                mAdapter.notifyDataSetChanged();
                if(mListener!=null)
                    mListener.onLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(mListener!=null)
                    mListener.onLoaded();
            }
        });
    }

    private void loadNext() {
        Query mNewQuery = mQuery.startAt(null,mLastKey).limitToFirst(nextItems);
        mNewQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int c=0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    T data = ds.getValue(tClass);
                    mLastKey = ds.getKey();
                    if(c!=0)
                        mList.add(data);
                    c++;
                }
                mAdapter.notifyDataSetChanged();
                isLoading = false;
                if(mListener != null)
                    mListener.onLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                isLoading = false;
                if(mListener != null)
                    mListener.onLoaded();
            }
        });
    }
}
