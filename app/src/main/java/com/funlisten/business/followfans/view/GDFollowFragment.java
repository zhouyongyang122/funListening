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
import com.funlisten.business.followfans.contract.GDFollowContract;
import com.funlisten.business.followfans.contract.ZYFollowOrUnFollowContract;
import com.funlisten.business.followfans.view.viewholder.GDFollowItemVH;
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

public class GDFollowFragment extends ZYListDateFragment<GDFollowContract.IPresenter,ZYUserList> implements GDFollowContract.IView {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        GDFollowItemVH.followOrUnFollowContract = zyFollowOrUnFollowContract;
        View view = super.onCreateView(inflater, container, savedInstanceState);
        int space = ZYScreenUtils.dp2px(mActivity, 15);
        mRefreshRecyclerView.getRecyclerView().setPadding(space, 0, 0, 0);
        mRefreshRecyclerView.getRecyclerView().setBackgroundColor(ZYResourceUtils.getColor(R.color.c8));
        return view;
    }

    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    ZYFollowOrUnFollowContract zyFollowOrUnFollowContract = new ZYFollowOrUnFollowContract() {
        @Override
        public void onFollow(ZYUserList data) {
            mSubscriptions.add(ZYNetSubscription.subscription(ZYNetManager.shareInstance().getApi().follow(data.toUserId+""),new ZYNetSubscriber<ZYResponse>(){
                @Override
                public void onSuccess(ZYResponse response) {
                    ZYLog.i(response.data.toString());
                }

                @Override
                public void onFail(String message) {
                    ZYLog.d(message);
                }
            }));
        }

        @Override
        public void onUnFollow(ZYUserList data) {
            mSubscriptions.add(ZYNetSubscription.subscription(ZYNetManager.shareInstance().getApi().followCancle(data.toUserId+""),new ZYNetSubscriber<ZYResponse>(){
                @Override
                public void onSuccess(ZYResponse response) {
                    ZYLog.i(response.data.toString());
                }

                @Override
                public void onFail(String message) {
                    ZYLog.d(message);
                }

            }));
        }
    };

    @Override
    protected void onItemClick(View view, int position) {

    }

    @Override
    protected ZYBaseViewHolder<ZYUserList> createViewHolder() {
        return new GDFollowItemVH();
    }



}
