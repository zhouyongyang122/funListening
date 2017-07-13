package com.funlisten.business.play.contract;

import com.funlisten.base.mvp.ZYIBasePresenter;
import com.funlisten.base.mvp.ZYIBaseView;

/**
 * Created by ZY on 17/7/10.
 */

public interface ZYPlayContract {

    interface IView extends ZYIBaseView<IPresenter>{

    }

    interface IPresenter extends ZYIBasePresenter{}

}
