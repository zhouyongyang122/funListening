package com.funlisten.business.search.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;

import java.util.List;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/19.
 */

public class ZYSearchModel extends ZYBaseModel {

    public  Observable<ZYResponse<List<String>>> getSearchWords(){
        return mApi.getSearchWords();
    }

   public Observable<ZYResponse<ZYListResponse<ZYAlbumDetail>>> getSearch(String type,String keyword){
       return mApi.getSearch(type,keyword);
    }

}
