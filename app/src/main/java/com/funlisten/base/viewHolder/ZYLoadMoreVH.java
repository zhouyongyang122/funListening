package com.funlisten.base.viewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.view.ZYILoadMoreView;

import butterknife.Bind;

/**
 * Created by ZY on 17/3/16.
 */

public class ZYLoadMoreVH extends ZYBaseViewHolder<ZYLoadMoreVH.LoadMore> implements ZYILoadMoreView {

    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.tv_no_more)
    TextView mTvNoMore;

    private LoadMore mLoadMore;

    public ZYLoadMoreVH() {
        mLoadMore = new LoadMore();
    }

    public ZYLoadMoreVH(LoadMore loadMore) {
        mLoadMore = loadMore;
    }

    public void showLoading() {
        mLoadMore.showWhat = LoadMore.SHOW_LOADING;
        updateView(mLoadMore, 0);
    }

    public void showNoMore() {
        mLoadMore.showWhat = LoadMore.SHOW_NO_MORE;
        updateView(mLoadMore, 0);
    }

    public void showError() {
        mLoadMore.showWhat = LoadMore.SHOW_ERROR;
        updateView(mLoadMore, 0);
    }

    public void hide() {
        mLoadMore.showWhat = LoadMore.HIDE;
        updateView(mLoadMore, 0);
    }


    @Override
    public void updateView(LoadMore data, int position) {
        if (mLoadMore != null && mItemView != null) {
            mItemView.setVisibility(View.VISIBLE);
            switch (mLoadMore.showWhat) {
                case LoadMore.SHOW_LOADING:
                    mProgressBar.setVisibility(View.VISIBLE);
                    mTvNoMore.setVisibility(View.GONE);
                    break;
                case LoadMore.SHOW_NO_MORE:
                    mProgressBar.setVisibility(View.GONE);
                    mTvNoMore.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(mLoadMore.noMoreText)) {
                        mTvNoMore.setText(mLoadMore.noMoreText);
                    }
                    break;
                case LoadMore.HIDE:
                default:
                    mItemView.setVisibility(View.GONE);
                    break;
            }
        }
    }

    public void setLoadMore(LoadMore loadMore) {
        mLoadMore = loadMore;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_default_load_more;
    }

    @Override
    public View getView() {
        return mItemView;
    }

    @Override
    public void setNoMoreText(String text) {
        mLoadMore.noMoreText = text;
    }

    public static class LoadMore {
        public static final int SHOW_LOADING = 1;
        public static final int SHOW_NO_MORE = 2;
        public static final int SHOW_ERROR = 3;
        public static final int HIDE = 4;
        public int showWhat;
        public String noMoreText;
    }
}
