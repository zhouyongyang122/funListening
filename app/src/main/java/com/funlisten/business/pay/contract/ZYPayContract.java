package com.funlisten.business.pay.contract;

import com.funlisten.base.mvp.ZYIBasePresenter;
import com.funlisten.base.mvp.ZYIBaseView;

/**
 * Created by gd on 2017/7/29.
 */

public interface ZYPayContract {
    interface View<P> extends ZYIBaseView<P> {
        void onPayFaild();
    }

    interface Presenter<D> extends ZYIBasePresenter {
        void getSignALiAudio(String json);
        void getWeChatSign(String productDetail);
    }
}
