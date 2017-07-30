package com.funlisten.business.persondata.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.user.model.ZYProvince;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by gd on 2017/7/23.
 */

public class ZYPersonModel extends ZYBaseModel {

    public  Observable<ZYResponse> updateUserAvatar(File photo){
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), photo);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("avatarFile", "photo.jpg", requestFile);
        return  mApi.updateUserAvatar(body);
    }

    public  Observable<ZYResponse<List<ZYProvince>>> getCities(){
        return mApi.getCities();
    }

    public  Observable<ZYResponse> updateUserDetail(Map<String, String> paramas){
        return mApi.updateUserDetail(paramas);
    }

}
