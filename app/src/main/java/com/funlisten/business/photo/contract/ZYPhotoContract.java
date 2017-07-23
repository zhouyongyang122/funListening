package com.funlisten.business.photo.contract;

import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.photo.ZYPhoto;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/15.
 */

public interface ZYPhotoContract {
    interface IView extends ZYListDataContract.View<ZYPhotoContract.IPresenter> {
    }

    interface IPresenter extends ZYListDataContract.Presenter<ZYPhoto> {
        void upLoadPhoto(File data);
        void deletePhoto(ArrayList<ZYPhoto> list);
    }

}
