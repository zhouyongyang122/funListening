package com.funlisten.business.main.contract;

import com.funlisten.base.mvp.ZYIBasePresenter;
import com.funlisten.base.mvp.ZYIBaseView;
import com.funlisten.business.main.model.bean.ZYVersion;

/**
 * Created by ZY on 17/3/16.
 */

public interface ZYMainContract {

    interface IView extends ZYIBaseView<IPresenter> {

        void showUpdateView(ZYVersion version);

    }

    interface IPresenter extends ZYIBasePresenter {
        void getVersion();
    }
}
