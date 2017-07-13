package com.funlisten.business.album.contract;

import com.funlisten.base.mvp.ZYIBasePresenter;
import com.funlisten.base.mvp.ZYIBaseView;
import com.funlisten.business.album.model.bean.ZYCategory;

import java.util.List;

/**
 * Created by ZY on 17/6/13.
 */

public interface ZYAlbumListHomeContract {

    interface IView extends ZYIBaseView<IPresenter>{

        void refreshCategorys(List<ZYCategory> mCategorys);
    }

    interface IPresenter extends ZYIBasePresenter{
        void loadData();
    }
}
