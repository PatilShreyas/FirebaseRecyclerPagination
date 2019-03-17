package com.shreyaspatil.firebase.recyclerpagination.listener;

public interface StateChangedListener {
    void onInitLoading();
    void onLoading();
    void onLoaded();
    void onFinished();
    void onError();
}
