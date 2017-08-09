package com.funlisten.business.myorder.contract;

import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.favorite.ZYFavorite;
import com.funlisten.business.order.ZYOrder;

/**
 * Created by Administrator on 2017/7/22.
 */

public interface ZYMyOrderContract {
    interface IView extends ZYListDataContract.View<ZYMyOrderContract.IPresenter> {

    }
    interface IPresenter extends ZYListDataContract.Presenter<ZYFavorite> {}
}
