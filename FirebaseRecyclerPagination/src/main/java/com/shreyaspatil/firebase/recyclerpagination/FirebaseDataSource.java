package com.shreyaspatil.firebase.recyclerpagination;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class FirebaseDataSource<T> extends PageKeyedDataSource<String,T> {

    private Query mQuery;
    private Class<T> mClass;
    private String mLastKey;
    private final MutableLiveData<LoadingState> mLoadingState = new MutableLiveData<>();


    private static final String TAG = "FirebaseDataSource";

    public static class Factory<T> extends DataSource.Factory<String, Class<T>> {

        private final Query mQuery;
        private final Class mClass;

        public Factory(@NonNull Query query, @NotNull Class<T> modelClass) {
            mQuery = query;
            mClass = modelClass;
        }

        @Override
        @NonNull
        public DataSource<String, Class<T>> create() {
            return new FirebaseDataSource(mQuery,mClass);
        }
    }

    FirebaseDataSource(Query mQuery, Class<T> modelClass){
        this.mQuery = mQuery;
        this.mClass = modelClass;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, T> callback) {
        // Set initial loading state
        mLoadingState.postValue(LoadingState.LOADING_INITIAL);

        Query mInitQuery = mQuery.limitToFirst(params.requestedLoadSize);
        mInitQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<T> mList = new ArrayList<T>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    T data = ds.getValue(mClass);
                    mLastKey = ds.getKey();
                    mList.add(data);
                }

                //Initial Load Success
                mLoadingState.postValue(LoadingState.LOADED);
                callback.onResult(mList,mLastKey,mLastKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Error Occured
                mLoadingState.postValue(LoadingState.ERROR);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, T> callback) {
        //Pending
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull final LoadCallback<String, T> callback) {
        // Set loading state
        mLoadingState.postValue(LoadingState.LOADING_MORE);

        Query mNewQuery = mQuery.startAt(null,params.key).limitToFirst(params.requestedLoadSize);
        mNewQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<T> mList = new ArrayList<T>();
                int c=0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    T data = ds.getValue(mClass);
                    mLastKey = ds.getKey();
                    if(c!=0)
                        mList.add(data);
                    c++;
                }

                mLoadingState.postValue(LoadingState.LOADED);

                //Detect End of Data
                if(mList.isEmpty())
                    mLoadingState.postValue(LoadingState.FINISHED);

                callback.onResult(mList,mLastKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mLoadingState.postValue(LoadingState.ERROR);
            }
        });
    }

    @NonNull
    public LiveData<LoadingState> getLoadingState() {
        return mLoadingState;
    }
}
