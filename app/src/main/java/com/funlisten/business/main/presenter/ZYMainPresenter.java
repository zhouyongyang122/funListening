package com.funlisten.business.main.presenter;

import com.funlisten.ZYApplication;
import com.funlisten.ZYPreferenceHelper;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.business.main.contract.ZYMainContract;
import com.funlisten.business.main.model.ZYMainModel;
import com.funlisten.business.main.model.bean.ZYVersion;
import com.funlisten.utils.ZYSystemUtils;

/**
 * Created by ZY on 17/5/10.
 */

public class ZYMainPresenter extends ZYBasePresenter implements ZYMainContract.IPresenter {

    ZYMainContract.IView mIView;

    ZYMainModel mModel;

    long lastGetVersionTime;

    public ZYMainPresenter(ZYMainContract.IView iView) {
        mIView = iView;
        mModel = new ZYMainModel();
        mIView.setPresenter(this);
    }

    public void getVersion() {
        if (lastGetVersionTime <= 0 && System.currentTimeMillis() - lastGetVersionTime > 10 * 60 * 1000) {
            lastGetVersionTime = System.currentTimeMillis();
            mSubscriptions.add(ZYNetSubscription.subscription(mModel.getVersion(), new ZYNetSubscriber<ZYResponse<ZYVersion>>() {
                        @Override
                        public void onSuccess(ZYResponse<ZYVersion> response) {
                            if (response.data == null) {
                                lastGetVersionTime = 0;
                            } else {
                                ZYVersion version = response.data;
                                if (version.versioncode > ZYSystemUtils.getAppVersionCode(ZYApplication.getInstance().getCurrentActivity())) {
                                    mIView.showUpdateView(version);
                                }
                            }
                        }

                        @Override
                        public void onFail(String message) {
                            lastGetVersionTime = 0;
                        }
                    }

            ));
        }
    }
}
