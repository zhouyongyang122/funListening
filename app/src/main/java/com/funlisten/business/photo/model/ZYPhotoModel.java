package com.funlisten.business.photo.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.photo.ZYPhoto;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by gd on 2017/7/15.
 */

public class ZYPhotoModel extends ZYBaseModel {
    public Observable<ZYResponse<ZYListResponse<ZYPhoto>>> photos(String userId, int pageIndex, int pageSize) {
        return mApi.photos(userId, pageIndex, pageSize);
    }

    public Observable<ZYResponse<ZYPhoto>> addPhoto(File photo) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), photo);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("photo", "photo.jpg", requestFile);

        return mApi.addPhoto(body);
    }

    public Observable<ZYResponse> delPhoto(String ids){
        return mApi.delPhoto(ids);
    }

}
