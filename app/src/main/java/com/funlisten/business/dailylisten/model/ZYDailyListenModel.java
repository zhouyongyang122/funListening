package com.funlisten.business.dailylisten.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.play.model.bean.ZYAudio;

import rx.Observable;

/**
 * Created by Administrator on 2017/7/20.
 */

public class ZYDailyListenModel extends ZYBaseModel {
    public  Observable<ZYResponse<ZYListResponse<ZYAudio>>> getAudios( int pageIndex,int pageSize, int albumId,String direction){
        return  mApi.getAudios(pageIndex,pageSize,albumId,direction);
    }

    public Observable<ZYResponse<ZYAlbumDetail>> getAlbumDetail(int id){
        return mApi.getAlbumDetail(id);
    }

}
