package com.funlisten.base.mvp;

/**
 * Created by ZY on 17/3/14.
 */

public interface ZYIBasePresenter {

    void subscribe();

    void unsubscribe();

    void onResume();

    void onPause();

    void onDestroy();
}
