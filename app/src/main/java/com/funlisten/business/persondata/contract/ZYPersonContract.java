package com.funlisten.business.persondata.contract;

import com.funlisten.base.mvp.ZYIBasePresenter;
import com.funlisten.base.mvp.ZYIBaseView;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.play.model.bean.ZYAudio;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by ZY on 17/7/10.
 */

public interface ZYPersonContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void updateUser();
    }

    interface IPresenter extends ZYIBasePresenter {
        void  updateUserAvatar(File photo);
        void updateUserDetail(Map<String,String> paramas);
    }

}
