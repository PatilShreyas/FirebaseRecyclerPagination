package com.shreyaspatil.firebase.recyclerpagination;

import android.arch.core.util.Function;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.Transformations;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;
import com.shreyaspatil.firebase.recyclerpagination.listener.StateChangedListener;


public abstract class FirebaseRecyclerPagingAdapter<T,H extends RecyclerView.ViewHolder> extends PagedListAdapter<T,H> implements LifecycleObserver{

    private final String TAG = "PagingAdapter";
    private final LiveData<PagedList<T>> mPagedList;
    private final LiveData<LoadingState> mLoadingState;
    private final LiveData<FirebaseDataSource> mDataSource;
    private StateChangedListener mListener;

    //State Observer
    private final Observer<LoadingState> mStateObserver = new Observer<LoadingState>() {
        @Override
        public void onChanged(@Nullable LoadingState state) {
            if (state == null || mListener == null) {
                return;
            }

            switch (state){
                case LOADING_INITIAL: mListener.onInitLoading(); break;
                case LOADED: mListener.onLoaded(); break;
                case LOADING_MORE: mListener.onLoading(); break;
                case FINISHED: mListener.onFinished(); break;
                case ERROR: mListener.onError();break;
            }
        }
    };

    //Data Observer
    private final Observer<PagedList<T>> mDataObserver = new Observer<PagedList<T>>() {
        @Override
        public void onChanged(@Nullable PagedList<T> snapshots) {
            if (snapshots == null) {
                return;
            }

            submitList(snapshots);
        }
    };

    public FirebaseRecyclerPagingAdapter(FirebasePagingOptions options){
        super(options.getDiffCallback());

        mPagedList = options.getData();

        //Init Data Source
        mDataSource = Transformations.map(mPagedList,
                new Function<PagedList<T>, FirebaseDataSource>() {
                    @Override
                    public FirebaseDataSource apply(PagedList<T> input) {
                        return (FirebaseDataSource) input.getDataSource();
                    }
                });

        //Init Loading State
        mLoadingState = Transformations.switchMap(mPagedList,
                new Function<PagedList<T>, LiveData<LoadingState>>() {
                    @Override
                    public LiveData<LoadingState> apply(PagedList<T> input) {
                        FirebaseDataSource dataSource = (FirebaseDataSource) input.getDataSource();
                        return dataSource.getLoadingState();
                    }
                });

        if (options.getOwner() != null) {
            options.getOwner().getLifecycle().addObserver(this);
        }

    }

    //Start Listening
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void startListening() {
        mPagedList.observeForever(mDataObserver);
        mLoadingState.observeForever(mStateObserver);
    }

    //Stop Listening
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stopListening() {
        mPagedList.removeObserver(mDataObserver);
        mLoadingState.removeObserver(mStateObserver);
    }

    @Override
    public void onBindViewHolder(@NonNull H viewHolder, int position) {
        T model = getItem(position);
        onBindViewHolder(viewHolder, position, model);
    }

    protected abstract void onBindViewHolder(@NonNull H viewHolder, int position, @NotNull T model);

    public void setStateChangedListener(StateChangedListener mListener){
        this.mListener = mListener;
    }


}
