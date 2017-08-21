package com.funlisten.business.set.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.set.contract.ZYMsgContract;
import com.funlisten.business.set.model.bean.ZYMsg;
import com.funlisten.business.set.model.ZYSetModel;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

/**
 * Created by ZY on 17/4/9.
 */

public class SRSysMsgPresenter extends ZYListDataPresenter<ZYMsgContract.IView, ZYSetModel, ZYMsg> implements ZYMsgContract.IPresenter {

    public SRSysMsgPresenter(ZYMsgContract.IView view, ZYSetModel model) {
        super(view, model);
        mRows = 100;
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getSysMsgs(mPageIndex, mRows), new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYMsg>>>() {
            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYMsg>> response) {
                success(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }


}
