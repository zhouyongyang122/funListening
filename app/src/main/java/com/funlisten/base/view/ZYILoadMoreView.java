package com.funlisten.base.view;

import android.view.View;

/**
 * Created by ZY on 17/3/16.
 */

public interface ZYILoadMoreView {

    View getView();

    void showLoading();

    void showNoMore();

    void showError();

    void setNoMoreText(String text);

    void hide();
}
