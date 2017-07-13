package com.funlisten.business.main.contract;

import com.funlisten.base.mvp.ZYIBasePresenter;
import com.funlisten.base.mvp.ZYIBaseView;
import com.funlisten.business.main.model.bean.ZYHome;

/**
 * Created by ZY on 17/5/13.
 */

public interface ZYHomeContract {

    interface IView extends ZYIBaseView<IPresenter> {

        void refreshView(ZYHome home);
    }

    interface IPresenter extends ZYIBasePresenter {
        void loadData();
    }
}
