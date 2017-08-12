package com.funlisten.business.profile.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.business.photo.ZYPhoto;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/16.
 */

public class ZYProfileModel extends ZYBaseModel {

   public  Observable<ZYResponse<ZYListResponse<ZYAlbumDetail>>> getAlbums(int pageIndex, int pageSize,int publisherId){
       return mApi.getAlbumTwo(pageIndex,pageSize,publisherId);
   }

    public Observable<ZYResponse<ZYUser>> getUserInfo( String userId){
        return mApi.getUserInfo(userId);
    }

    public Observable<ZYResponse<ZYListResponse<ZYPhoto>>> photos(String userId){
        return  mApi.photos(userId,1,4);
    }

}
