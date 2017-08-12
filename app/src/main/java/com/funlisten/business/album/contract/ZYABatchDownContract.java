package com.funlisten.business.album.contract;

import com.funlisten.base.mvp.ZYIBasePresenter;
import com.funlisten.base.mvp.ZYIBaseView;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.play.model.bean.ZYAudio;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/7/4.
 */

public interface ZYABatchDownContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void showDatas(ArrayList<ZYAudio> datas);
    }

    interface IPresenter extends ZYIBasePresenter {
        ArrayList<ZYAudio> getDatas();

        ZYAlbumDetail getAlbumDetail();
    }
}
