package com.funlisten.business.album.contract;

import com.funlisten.base.mvp.ZYIBasePresenter;
import com.funlisten.base.mvp.ZYIBaseView;
import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYComment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/7/4.
 */

public interface ZYAlbumDetailContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void showDatas(ArrayList<Object> datas);

        void suportOk();

        void suortCancle();
    }

    interface IPresenter extends ZYIBasePresenter {
        void loadComments(List<ZYAlbumDetail.Detail> details);

        ArrayList<Object> getDatas();

        void suport(ZYComment comment);

        void suportCancle(ZYComment comment);
    }
}
