package com.funlisten.business.main.model;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.business.main.model.bean.ZYHome;
import com.funlisten.business.main.model.bean.ZYVersion;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ZY on 17/5/10.
 */

public class ZYMainModel extends ZYBaseModel {

    public Observable<ZYResponse<ZYVersion>> getVersion() {
        return mApi.getVersion();
    }

    public Observable<ZYResponse<ZYHome>> getHomeData() {
        return mApi.getHomeData();
    }

    public Observable<ZYResponse<ZYUser>> getUserInfo(String userId){
        return mApi.getUserInfo(userId);
    }
}
