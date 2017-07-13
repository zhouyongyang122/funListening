package com.funlisten.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funlisten.R;
import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.view.ZYRefreshListener;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;

/**
 * Created by ZY on 17/3/30.
 */

public abstract class ZYListDateFragment<P extends ZYListDataContract.Presenter<D>, D> extends ZYBaseRecyclerFragment<P>
        implements ZYListDataContract.View<P> {

    protected ZYBaseRecyclerAdapter<D> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        init();

        return rootView;
    }

    protected abstract void onItemClick(View view, int position);

    protected ZYBaseRecyclerAdapter<D> createAdapter() {
        return new ZYBaseRecyclerAdapter<D>(mPresenter.getDataList()) {
            @Override
            public ZYBaseViewHolder<D> createViewHolder(int type) {
                return ZYListDateFragment.this.createViewHolder();
            }
        };
    }

    protected abstract ZYBaseViewHolder<D> createViewHolder();

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    protected void init() {
        mAdapter = createAdapter();

        mRefreshRecyclerView.getSwipeRefreshLayout().setColorSchemeResources(R.color.c2);
        mRefreshRecyclerView.setLayoutManager(getLayoutManager());
        mRefreshRecyclerView.setAdapter(mAdapter);

        mRefreshRecyclerView.setRefreshListener(new ZYRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refresh();
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore();
            }
        });

        mAdapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ZYListDateFragment.this.onItemClick(view, position);
            }
        });
    }
}
