package com.funlisten.business.set.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.set.contract.ZYMsgContract;
import com.funlisten.business.set.model.bean.ZYMsg;

/**
 * Created by ZY on 17/4/9.
 */

public class ZYMsgFragment extends ZYListDateFragment<ZYMsgContract.IPresenter, ZYMsg> implements ZYMsgContract.IView {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRefreshRecyclerView.getLoadingView().setEmptyText("还没有消息哦");
        return view;
    }

    @Override
    protected void onItemClick(View view, int position) {

    }

    @Override
    public void showList(boolean isHasMore) {
        super.showList(false);
    }

    @Override
    protected ZYBaseViewHolder<ZYMsg> createViewHolder() {
        return new ZYMsgVH();
    }
}
