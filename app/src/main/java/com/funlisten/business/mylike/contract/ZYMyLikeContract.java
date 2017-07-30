package com.funlisten.business.mylike.contract;

import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.favorite.ZYFavorite;
import com.funlisten.business.play.model.bean.ZYAudio;

/**
 * Created by gd on 2017/7/12.
 */

public interface ZYMyLikeContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
        void refreshView(int totalCount);
        void loadAudio(ZYAlbumDetail albumDetail, ZYAudio audio);
    }

    interface IPresenter extends ZYListDataContract.Presenter<ZYFavorite> {
        void getAlbumDetail(ZYAudio audio);
    }
}
