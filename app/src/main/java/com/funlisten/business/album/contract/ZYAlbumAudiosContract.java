package com.funlisten.business.album.contract;

import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.play.model.bean.ZYAudio;

/**
 * Created by ZY on 17/7/5.
 */

public interface ZYAlbumAudiosContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
       void  refreshBuy(boolean isBuy);
    }

    interface IPresenter extends ZYListDataContract.Presenter<ZYAudio> {
        void setSortType(String sortType);

        String getSortType();

        void changerSortType();

        void choiceEpisode(int start);

        int getTotalCount();

        void isOrder(String objectId);
    }
}
