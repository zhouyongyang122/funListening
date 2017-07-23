package com.funlisten.business.myorder.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.myorder.contract.ZYMyOrderContract;
import com.funlisten.business.myorder.view.viewholder.ZYMyOrderVH;
import com.funlisten.business.order.ZYOrder;
import com.funlisten.utils.ZYResourceUtils;
import com.funlisten.utils.ZYScreenUtils;

/**
 * Created by Administrator on 2017/7/22.
 */

public class ZYMyOrderFragment extends ZYListDateFragment<ZYMyOrderContract.IPresenter,ZYOrder> implements ZYMyOrderContract.IView{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        int space = ZYScreenUtils.dp2px(mActivity, 15);
        mRefreshRecyclerView.getRecyclerView().setPadding(space, 0, 0, 0);
        mRefreshRecyclerView.getRecyclerView().setBackgroundColor(ZYResourceUtils.getColor(R.color.c8));
        return view;
    }

    @Override
    protected void onItemClick(View view, int position) {

    }

    @Override
    protected ZYBaseViewHolder<ZYOrder> createViewHolder() {
        return new ZYMyOrderVH();
    }
}
