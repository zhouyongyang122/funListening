package com.funlisten.business.profile.contract;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.business.photo.ZYPhoto;

import java.util.List;

/**
 * Created by gd on 2017/7/12.
 */

public interface ZYProfileContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
        void refreshView();
        void refreshFollow(ZYUser user);
    }

    interface IPresenter extends ZYListDataContract.Presenter<ZYAlbumDetail> {
        ZYListResponse<ZYPhoto> getZyPhotoList();

        ZYUser getUser();

        int getTotalCount();

        void follow(String userId);

        void unFollow(String userId);
    }
}
