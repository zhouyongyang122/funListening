package com.funlisten.business.album.contract;

import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;

/**
 * Created by ZY on 17/6/13.
 */

public interface ZYAlbumListContract {

    interface IView extends ZYListDataContract.View<IPresenter>{

    }

    interface IPresenter extends ZYListDataContract.Presenter<ZYAlbumDetail>{

    }
}
