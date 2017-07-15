package com.funlisten.business.photo.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.photo.ZYPhoto;

import retrofit2.http.Field;
import rx.Observable;

/**
 * Created by gd on 2017/7/15.
 */

public class ZYPhotoModel extends ZYBaseModel {
    public Observable<ZYResponse<ZYListResponse<ZYPhoto>>> photos(String userId, int pageIndex, int pageSize){
        return mApi.photos(userId,pageIndex,pageSize);
    }
    public Observable<ZYResponse<ZYPhoto>> addPhoto( byte[] photo){
        return mApi.addPhoto(photo);
    }
}
