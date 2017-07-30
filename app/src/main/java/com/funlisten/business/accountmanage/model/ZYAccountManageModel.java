package com.funlisten.business.accountmanage.model;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/24.
 */

public class ZYAccountManageModel extends ZYBaseModel {
    public  Observable<ZYResponse> getCode(String phone, String type){
        return mApi.getCode(phone,type);
    }
    public Observable<ZYResponse> checkCode(String phone,String type, String code){
        return mApi.checkCode(phone,type,code);
    }
    public Observable<ZYResponse> updatePass(String oldPass, String newPass){
        return mApi.updatePass(oldPass,newPass);
    }
    public Observable<ZYResponse> findPass(String phone,String code,String password){
        return mApi.findPass(phone,code,password);
    }
}
