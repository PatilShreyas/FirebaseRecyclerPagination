package com.shreyaspatil.firebase.recyclerpagination.listener;

import com.google.firebase.database.DatabaseError;

public interface StateChangedListener {
    void onInitLoading();
    void onLoading();
    void onLoaded();
    void onFinished();
    void onError(DatabaseError databaseError);
}
