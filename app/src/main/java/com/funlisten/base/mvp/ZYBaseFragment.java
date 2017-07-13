package com.funlisten.base.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.funlisten.base.view.ZYWaitDialog;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by ZY on 17/3/14.
 */

public class ZYBaseFragment<P extends ZYIBasePresenter> extends Fragment implements ZYIBaseView<P> {

    protected P mPresenter;

    protected Activity mActivity;

    protected ZYWaitDialog mWaitDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            if (mPresenter != null) {
                mPresenter.subscribe();
            }
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "onViewCreated-error:" + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ZYLog.e(getClass().getSimpleName(), "onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (mPresenter != null) {
                mPresenter.unsubscribe();
            }
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "onDestroyView-error:" + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZYLog.e(getClass().getSimpleName(), "onDestroy");
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {

        }
        try {
            ButterKnife.unbind(this);
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "onDestroy-error:" + e.getMessage());
        }
    }

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    protected void showWaitDialog(final String message) {
        if (mWaitDialog == null) {
            mWaitDialog = new ZYWaitDialog(mActivity);
        }
        mWaitDialog.showWidthMessage(message);

    }

    protected void refreshWaitDialog(String message) {
        mWaitDialog.refreshMessage(message);
    }

    protected void hideWaitDialog() {
        if (mWaitDialog != null && mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }

    }

    @Override
    public void showProgress() {
        showWaitDialog(null);
    }

    @Override
    public void hideProgress() {
        hideWaitDialog();
    }

    @Override
    public void showToast(String msg) {
        ZYToast.show(mActivity, msg);
    }

    @Override
    public void finish() {
        mActivity.finish();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {

    }
}
