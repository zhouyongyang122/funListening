package com.funlisten.business.dailylisten.contract;

import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.play.model.bean.ZYAudio;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/20.
 */

public interface ZYDailyListenContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
        void showDatas(ArrayList<Object> data);
        void loadAudio(ZYAlbumDetail albumDetail,ZYAudio audio);
    }

    interface IPresenter extends ZYListDataContract.Presenter<Object> {
        ArrayList<Object> getDataList();
        void getAlbumDetail(ZYAudio audio);
    }
}
