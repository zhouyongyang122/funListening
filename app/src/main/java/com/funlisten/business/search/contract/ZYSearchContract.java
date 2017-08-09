package com.funlisten.business.search.contract;

import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.dailylisten.contract.ZYDailyListenContract;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.search.model.bean.ZYAudioAndAlbumInfo;
import com.funlisten.business.user.model.ZYUserList;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public interface ZYSearchContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
        void showHotWord(List<String> data);
    }

    interface IPresenter extends ZYListDataContract.Presenter<String>{
        void loadHotWord();
        void loadSearch(String type,String keyword);
    }

    interface AudioView extends ZYListDataContract.View<AudioPresenter> {
        void refreshHeader(int totalCount);
        void loadAudio(ZYAlbumDetail albumDetail,ZYAudio audio);
    }

    interface AudioPresenter extends ZYListDataContract.Presenter<ZYAudioAndAlbumInfo>{
        void getAlbumDetail(final ZYAudio audio);
        void search(String key);
    }

    interface AlbumView extends ZYListDataContract.View<AlbumPresenter> {
        void refreshHeader(int totalCount);
    }

    interface AlbumPresenter extends ZYListDataContract.Presenter<ZYAudioAndAlbumInfo>{
        void search(String key);
    }


}
