package com.funlisten.business.followfans.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funlisten.R;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.followfans.contract.ZYFollowContract;
import com.funlisten.business.followfans.view.viewholder.ZYFollowItemVH;
import com.funlisten.business.profile.activity.ZYProFlieActivity;
import com.funlisten.business.user.model.ZYUserList;
import com.funlisten.service.net.ZYNetManager;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYResourceUtils;
import com.funlisten.utils.ZYScreenUtils;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by gd on 2017/7/12.
 */

public class ZYFollowFragment extends ZYListDateFragment<ZYFollowContract.IPresenter, ZYUserList> implements ZYFollowContract.IView, ZYFollowItemVH.FollowItemListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        int space = ZYScreenUtils.dp2px(mActivity, 15);
        mRefreshRecyclerView.getRecyclerView().setPadding(space, 0, 0, 0);
        mRefreshRecyclerView.getRecyclerView().setBackgroundColor(ZYResourceUtils.getColor(R.color.c8));
        return view;
    }

    protected CompositeSubscription mSubscriptions = new CompositeSubscription();


    @Override
    protected void onItemClick(View view, int position) {
        ZYUserList user = mAdapter.getItem(position);
        startActivity(ZYProFlieActivity.createIntent(mActivity, user.user.id));
    }


    @Override
    protected ZYBaseViewHolder<ZYUserList> createViewHolder() {
        return new ZYFollowItemVH(this);
    }

    @Override
    public void onFollowClick(ZYUserList user, boolean follow) {
        if (follow) {
            mPresenter.follow(user);
        } else {
            mPresenter.followCancle(user);
        }
    }
}
