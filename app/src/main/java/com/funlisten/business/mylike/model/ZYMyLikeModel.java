package com.funlisten.business.mylike.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.favorite.ZYFavorite;

import rx.Observable;

/**
 * Created by gd on 2017/7/25.
 */

public class ZYMyLikeModel extends ZYBaseModel {
    public Observable<ZYResponse<ZYListResponse<ZYFavorite>>> getFavorites(String type, int pageIndex,int pageSize){
        return mApi.getFavorites(type,pageIndex,pageSize);
    }

    public Observable<ZYResponse<ZYAlbumDetail>> getAlbumDetail(int id){
        return mApi.getAlbumDetail(id);
    }
}
