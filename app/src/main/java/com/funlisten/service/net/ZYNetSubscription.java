package com.funlisten.service.net;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ZY on 17/3/16.
 */

public class ZYNetSubscription {

    public static Subscription subscription(@NonNull Observable observable, @NonNull Subscriber subscriber) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
