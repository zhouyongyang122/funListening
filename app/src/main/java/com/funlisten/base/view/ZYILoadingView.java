package com.funlisten.base.view;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ZY on 17/3/16.
 */

public interface ZYILoadingView {

    void showLoading();

    void showLoading(String loadingText);

    void showError();

    void showError(String errorText);

    void showEmpty();

    void showEmpty(String emptyText);

    void showNothing();

    void setLoadingView(View view);

    void setEmptyView(View view);

    void setEmptyText(String text);

    void setEmptyIcon(int iconRes);

    void setErrorView(View view);

    void setErrorText(String text);

    void setErrorIcon(int iconRes);

    void setRetryListener(View.OnClickListener onClickListener);

    void attach(ViewGroup root);

    View getView();
}
