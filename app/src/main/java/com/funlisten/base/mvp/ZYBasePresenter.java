package com.funlisten.base.mvp;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/3/16.
 */

public class ZYBasePresenter implements ZYIBasePresenter {

    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.unsubscribe();
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }
}
