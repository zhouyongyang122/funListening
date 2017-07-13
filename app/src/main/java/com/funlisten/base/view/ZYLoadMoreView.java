package com.funlisten.base.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.funlisten.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/3/16.
 */

public class ZYLoadMoreView implements ZYILoadMoreView {
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.tv_no_more)
    TextView mTvNoMore;

    private View mRootView;

    private View.OnClickListener mOnClickListener;

    public ZYLoadMoreView(Context context){
        initView(context);
    }

    private void initView(Context context){
        mRootView = LayoutInflater.from(context).inflate(R.layout.sr_view_default_load_more, null);
        ButterKnife.bind(this, mRootView);
    }

    @NonNull
    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void showLoading() {
        mRootView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mTvNoMore.setVisibility(View.GONE);
    }

    @Override
    public void showNoMore() {
        mRootView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mTvNoMore.setVisibility(View.VISIBLE);
        mTvNoMore.setOnClickListener(null);
    }

    @Override
    public void showError() {
        mRootView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mTvNoMore.setText(R.string.default_loading_error);
        mTvNoMore.setOnClickListener(mOnClickListener);
    }

    @Override
    public void setNoMoreText(String text) {
        mTvNoMore.setText(text);
    }

    @Override
    public void hide() {
        mRootView.setVisibility(View.INVISIBLE);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
