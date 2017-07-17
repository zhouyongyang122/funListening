package com.funlisten.business.play.contract;

import com.funlisten.base.mvp.ZYIBasePresenter;
import com.funlisten.base.mvp.ZYIBaseView;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.play.model.bean.ZYAudio;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/7/10.
 */

public interface ZYPlayContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void refreshView(boolean needPlay);
    }

    interface IPresenter extends ZYIBasePresenter {
        List<Object> getComments();

        ZYAlbumDetail getAlbumDetail();

        ZYAudio getAudio();
    }

}
