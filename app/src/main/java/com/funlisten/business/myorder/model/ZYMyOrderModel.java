package com.funlisten.business.myorder.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.favorite.ZYFavorite;
import com.funlisten.business.order.ZYOrder;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by gd on 2017/7/22.
 */

public class ZYMyOrderModel extends ZYBaseModel {
    public Observable<ZYResponse<ZYListResponse<ZYOrder>>> getorders(String type, int pageIndex, int pageSize){
        return mApi.getorders(type,pageIndex,pageSize);
    }

   public Observable<ZYResponse<ZYListResponse<ZYFavorite>>> getFavorites(String type,int pageIndex, int pageSize){
       return mApi.getFavorites(type,pageIndex,pageSize);
    }

}
