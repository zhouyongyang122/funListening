package com.funlisten.base.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.funlisten.R;
import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.viewHolder.ZYLoadMoreVH;

/**
 * Created by ZY on 17/3/16.
 */

public class ZYSwipeRefreshRecyclerView extends RelativeLayout implements ZYISwipeRecyclerView, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ZYRefreshListener mRefreshListener;
    private RecyclerView mRecyclerView;
    private ZYLoadMoreVH mMoreViewHolder;
    private ZYILoadingView mLoadingView;
    private ZYBaseRecyclerAdapter mAdapter;

    private boolean mIsLoading;
    private boolean mIsHasMore;
    private boolean mIsRefresh;
    private boolean mIsLoadMoreEnable = true;

    public ZYSwipeRefreshRecyclerView(Context context) {
        super(context);
        init();
    }

    public ZYSwipeRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZYSwipeRefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.sr_view_swipe_recycer, this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mMoreViewHolder = new ZYLoadMoreVH();
        mLoadingView = new ZYLoadingView(getContext());
        mLoadingView.attach(this);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    public void setRefreshListener(ZYRefreshListener refreshListener) {
        mRefreshListener = refreshListener;
    }

    @Override
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        mIsLoadMoreEnable = loadMoreEnable;
    }

    @Override
    public void setRefreshEnable(boolean refreshEnable) {
        mSwipeRefreshLayout.setEnabled(refreshEnable);
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        mIsLoading = true;
        mIsRefresh = true;
        mSwipeRefreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public boolean isRefresh() {
        return mIsRefresh;
    }

    @Override
    public void setLayoutManager(@NonNull final RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mAdapter != null && mAdapter.getFooterSize() != 0) {
                        if (position == mAdapter.getItemCount() - 1) {
                            return gridLayoutManager.getSpanCount();
                        } else {
                            return 1;
                        }
                    }
                    return 1;
                }
            });
        }
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = getLastPosition();

                if (!mIsLoading
                        && mIsHasMore
                        && mAdapter.getItemCount() == (lastPosition + 1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mIsLoading = true;
                    mIsRefresh = false;
                    if (mRefreshListener != null) {
                        mMoreViewHolder.showLoading();
                        mRefreshListener.onLoadMore();
                    }
                } else if (!mIsHasMore) {
                    showNoMore();
                }
            }
        });
    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void setLoadingView(@NonNull ZYILoadingView loadingView) {
        this.removeView(loadingView.getView());
        mLoadingView = loadingView;
        mLoadingView.attach(this);
    }

    @Override
    public ZYILoadingView getLoadingView() {
        return mLoadingView;
    }

    @Override
    public ZYILoadMoreView getLoadMoreView() {
        return mMoreViewHolder;
    }

    @Override
    public void setAdapter(@NonNull ZYBaseRecyclerAdapter adapter) {
        mAdapter = adapter;
        if (mIsLoadMoreEnable) {
            mAdapter.addFooter(mMoreViewHolder);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setMoreViewHolder(@NonNull ZYLoadMoreVH moreViewHolder) {
        mMoreViewHolder = moreViewHolder;
    }

    @Override
    public void showList(boolean isHasMore) {
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        mIsLoading = false;
        mIsHasMore = isHasMore;
        mLoadingView.showNothing();
        mMoreViewHolder.hide();
        if (!isHasMore) {
            showNoMore();
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showEmpty() {
        mIsLoading = false;
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mLoadingView.showEmpty();
    }

    @Override
    public void showError() {
        mIsLoading = false;
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mLoadingView.showError();
    }

    @Override
    public void showLoading() {
        mIsLoading = true;
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mLoadingView.showLoading();
    }

    @Override
    public void onRefresh() {
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
        }
    }

    private int getFirstPosition() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        int firstPosition = 0;
        if (layoutManager instanceof LinearLayoutManager) {
            firstPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] firstPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findFirstVisibleItemPositions(firstPositions);
            firstPosition = firstPositions[0];
            for (int firstPositionTmp : firstPositions) {
                if (firstPosition > firstPositionTmp) {
                    firstPosition = firstPositionTmp;
                }
            }
        }

        return firstPosition;
    }

    private int getLastPosition() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        int lastPosition = 0;
        if (layoutManager instanceof LinearLayoutManager) {
            lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
            lastPosition = lastPositions[0];
            for (int lastPositionTmp : lastPositions) {
                if (lastPosition < lastPositionTmp) {
                    lastPosition = lastPositionTmp;
                }
            }
        }

        return lastPosition;
    }

    private void showNoMore() {
        if (getFirstPosition() == 0) {
            mMoreViewHolder.hide();
        } else {
            mMoreViewHolder.showNoMore();
        }
    }
}
