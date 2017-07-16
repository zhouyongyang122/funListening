package com.funlisten.base.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.utils.ZYScreenUtils;

/**
 * Created by ZY on 17/3/16.
 */

public class ZYLoadingView implements ZYILoadingView {

    private RelativeLayout mEmptyRoot;

    private View mViewEmpty;
    private View mViewLoading;
    private View mViewError;

    private TextView mTvEmpty;
    private ImageView mImgEmpty;
    private TextView mBtnEmpty;

    private TextView mTvLoading;
    private ProgressBar mProgressBar;

    private TextView mTvError;
    private ImageView mImgError;
    private View mLayoutError;

    private Context mContext;
    private LayoutInflater mInflater;

    private int mWidth;
    private int mHeight;

    public ZYLoadingView(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        init();
    }

    public ZYLoadingView(Context context, int w, int h) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mWidth = w;
        mHeight = h;
        init();
    }

    private void init() {
        mViewEmpty = mInflater.inflate(R.layout.sr_view_default_empty, null);
        mImgEmpty = (ImageView) mViewEmpty.findViewById(R.id.img_empty);
        mTvEmpty = (TextView) mViewEmpty.findViewById(R.id.tv_empty);
        mBtnEmpty = (TextView) mViewEmpty.findViewById(R.id.btn_empty);

        mViewError = mInflater.inflate(R.layout.sr_view_default_error, null);
        mImgError = (ImageView) mViewError.findViewById(R.id.img_error);
        mTvError = (TextView) mViewError.findViewById(R.id.tv_error);
        mLayoutError = mViewError.findViewById(R.id.layout_error);

        mViewLoading = mInflater.inflate(R.layout.sr_view_default_loading, null);
        mTvLoading = (TextView) mViewLoading.findViewById(R.id.tv_loading);
        mProgressBar = (ProgressBar) mViewLoading.findViewById(R.id.pb_loading);

        ViewGroup.LayoutParams lpEmptyRoot;
        if (mWidth == 0 && mHeight == 0) {
            lpEmptyRoot = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            lpEmptyRoot = new ViewGroup.LayoutParams(mWidth, mHeight);
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ZYScreenUtils.getScreenWidth(mContext));
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mEmptyRoot = new RelativeLayout(mContext);
        mEmptyRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mEmptyRoot.setBackgroundResource(R.color.c8);
        mEmptyRoot.setLayoutParams(lpEmptyRoot);
        mEmptyRoot.addView(mViewEmpty, lp);
        mEmptyRoot.addView(mViewError, lp);
        mEmptyRoot.addView(mViewLoading, lp);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        mEmptyRoot.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
    }

    @Override
    public void showLoading(String loadingText) {
        showLoading();
        if (!TextUtils.isEmpty(loadingText)) {
            mTvLoading.setText(loadingText);
        }
    }

    @Override
    public void showError() {
        mEmptyRoot.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);

        //还原错误信息
        mTvError.setText(mContext.getString(R.string.default_loading_error));
    }

    @Override
    public void showError(String errorText) {
        showError();
        if (!TextUtils.isEmpty(errorText)) {
            mTvError.setText(errorText);
        }
    }

    @Override
    public void showEmpty() {
        mEmptyRoot.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty(String emptyText) {
        showEmpty();
        if (!TextUtils.isEmpty(emptyText)) {
            mTvEmpty.setText(emptyText);
        }
    }

    @Override
    public void showNothing() {
        mEmptyRoot.setVisibility(View.GONE);
    }

    @Override
    public void setLoadingView(View view) {
        if (view != null) {
            mEmptyRoot.removeView(mViewLoading);
            mEmptyRoot.addView(view);
            mViewLoading = view;
            mViewLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void setEmptyView(View view) {
        if (view != null) {
            mEmptyRoot.removeView(mViewEmpty);
            mEmptyRoot.addView(view);
            mViewEmpty = view;
            mViewEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void setEmptyText(String text) {
        mTvEmpty.setText(text);
    }

    @Override
    public void setEmptyIcon(int iconRes) {
        mImgEmpty.setImageResource(iconRes);
    }

    /**
     * 显示界面的引导按钮
     *
     * @param onClickListener
     * @param text
     */
    public void showEmptyBtn(View.OnClickListener onClickListener, String text) {
        mBtnEmpty.setOnClickListener(onClickListener);
        mBtnEmpty.setText(text);
        mBtnEmpty.setVisibility(View.VISIBLE);
        showEmpty();
    }

    @Override
    public void setErrorView(View view) {
        if (view != null) {
            mEmptyRoot.removeView(mViewError);
            mEmptyRoot.addView(view);
            mViewError = view;
            mViewError.setVisibility(View.GONE);
        }
    }

    @Override
    public void setErrorText(String text) {
        mTvError.setText(text);
    }

    @Override
    public void setErrorIcon(int iconRes) {
        mImgError.setImageResource(iconRes);
    }

    @Override
    public void setRetryListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            mLayoutError.setOnClickListener(onClickListener);
        }
    }

    @Override
    public void attach(ViewGroup root) {
        if (root != null) {
            root.addView(mEmptyRoot);
        }
    }

    @Override
    public View getView() {
        return mEmptyRoot;
    }
}
