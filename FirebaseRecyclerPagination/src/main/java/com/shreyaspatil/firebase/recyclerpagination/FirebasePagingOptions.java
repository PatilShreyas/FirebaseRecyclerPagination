package com.shreyaspatil.firebase.recyclerpagination;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.google.firebase.database.Query;

public final class FirebasePagingOptions<T> {

    private final LiveData<PagedList<T>> mData;
    private final DiffUtil.ItemCallback<T> mDiffCallback;
    private final LifecycleOwner mOwner;

    private FirebasePagingOptions(@NonNull LiveData<PagedList<T>> data, @NonNull DiffUtil.ItemCallback<T> diffCallback, @Nullable LifecycleOwner owner) {
        mData = data;
        mDiffCallback = diffCallback;
        mOwner = owner;
    }

    @NonNull
    public LiveData<PagedList<T>> getData() {
        return mData;
    }

    @NonNull
    public DiffUtil.ItemCallback<T> getDiffCallback() {
        return mDiffCallback;
    }

    @Nullable
    public LifecycleOwner getOwner() {
        return mOwner;
    }

    public static final class Builder<T> {

        private LiveData<PagedList<T>> mData;
        private LifecycleOwner mOwner;
        private DiffUtil.ItemCallback<T> mDiffCallback;


        @NonNull
        public Builder<T> setQuery(@NonNull Query query, @NonNull PagedList.Config config, @NonNull Class<T> modelClass) {
            FirebaseDataSource.Factory factory = new FirebaseDataSource.Factory(query,modelClass);
            mData = new LivePagedListBuilder<>(factory, config).build();
            return this;
        }


        @NonNull
        public Builder<T> setDiffCallback(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
            mDiffCallback = diffCallback;
            return this;
        }

        @NonNull
        public Builder<T> setLifecycleOwner(@NonNull LifecycleOwner owner) {
            mOwner = owner;
            return this;
        }

        //This will make instance of FirebasePagingOptions
        @NonNull
        public FirebasePagingOptions<T> build() {
            if (mData == null) {
                throw new IllegalStateException("Must call setQuery() before calling build().");
            }

            if (mDiffCallback == null) {
                mDiffCallback = new DiffUtil.ItemCallback<T>() {
                    @Override
                    public boolean areItemsTheSame(T oldItem, T newItem) {
                        return oldItem.equals(newItem);
                    }

                    @Override
                    public boolean areContentsTheSame(T oldItem, T newItem) {
                        return oldItem.equals(newItem);
                    }
                };
            }

            return new FirebasePagingOptions<>(mData, mDiffCallback, mOwner);
        }

    }

}