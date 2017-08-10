package com.funlisten.business.followfans.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.followfans.contract.ZYFollowContract;
import com.funlisten.business.followfans.model.ZYFollowModel;
import com.funlisten.business.user.model.ZYUserList;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

import rx.Observable;

/**
 * Created by Administrator on 2017/7/12.
 */

public class ZYFollowPresenter extends ZYListDataPresenter<ZYFollowContract.IView, ZYFollowModel, ZYUserList> implements ZYFollowContract.IPresenter {
    private String userId;

    private int mType = ZYFollowModel.FOLLOW_TYPE;

    public ZYFollowPresenter(ZYFollowContract.IView view, String userId, int type) {
        super(view, new ZYFollowModel());
        this.userId = userId;
        mType = type;
    }

    @Override
    protected void loadData() {

        Observable<ZYResponse<ZYListResponse<ZYUserList>>> observable = null;

        if (mType == ZYFollowModel.FOLLOW_TYPE) {
            observable = mModel.follows(userId, mPageIndex, mRows);
        } else {
            observable = mModel.fans(userId, mPageIndex, mRows);
        }

        mSubscriptions.add(ZYNetSubscription.subscription(observable, new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYUserList>>>() {
            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYUserList>> response) {
                success(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }

    public void follow(final ZYUserList user) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.follow(user.user.userId), new ZYNetSubscriber<ZYResponse<Object>>() {
            @Override
            public void onSuccess(ZYResponse<Object> response) {
                user.followStatus = ZYBaseModel.FOLLOW_HAS_STATE;
            }

            @Override
            public void onFail(String message) {
            }
        }));
    }

    public void followCancle(final ZYUserList user) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.followCancle(user.user.userId), new ZYNetSubscriber<ZYResponse<Object>>() {
            @Override
            public void onSuccess(ZYResponse<Object> response) {
                user.followStatus = ZYBaseModel.FOLLOW_NO_STATE;
            }

            @Override
            public void onFail(String message) {
            }
        }));
    }
}
