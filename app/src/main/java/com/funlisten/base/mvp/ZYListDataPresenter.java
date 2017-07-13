package com.funlisten.base.mvp;

import com.funlisten.ZYApplication;
import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.utils.ZYToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/3/30.
 */

public abstract class ZYListDataPresenter<V extends ZYListDataContract.View, M, D> extends ZYBasePresenter
        implements ZYListDataContract.Presenter<D> {

    protected V mView;
    protected M mModel;

    protected List<D> mDataList = new ArrayList<>();

    protected int mPageIndex = 1;
    protected int mRows = 10;

    protected boolean isFristLoad = true;

    public ZYListDataPresenter(V view, M model) {
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        super.subscribe();
        mView.showLoading();
        refresh();
    }

    @Override
    public void refresh() {
        mPageIndex = 1;
        loadData();
    }

    @Override
    public void loadMore() {
        mPageIndex += 1;
        loadData();
    }

    @Override
    public List<D> getDataList() {
        return mDataList;
    }

    protected abstract void loadData();

    protected void success(ZYResponse<ZYListResponse<D>> response) {
        isFristLoad = false;
        ZYListResponse<D> listResponse = response.data;
        List<D> dataList = listResponse.data;
        success(dataList, listResponse.totalPage > listResponse.pageIndex);
    }

    protected void success(List<D> dataList, boolean hasNext) {
        isFristLoad = false;
        if (isRefresh()) {
            mDataList.clear();
        }
        if (dataList != null && !dataList.isEmpty()) {
            mDataList.addAll(dataList);
            mView.showList(hasNext);
        } else if (mDataList.isEmpty()) {
            mView.showEmpty();
        } else {
            mView.showList(false);
        }
    }

    protected void fail(String message) {
        if (isFristLoad) {
            mView.showError();
        } else {
            mView.showList(true);
            ZYToast.show(ZYApplication.getInstance(), message == null ? "网络异常,请重新尝试!" : message);
        }
    }

    public boolean isRefresh() {
        return mPageIndex == 1;
    }
}
