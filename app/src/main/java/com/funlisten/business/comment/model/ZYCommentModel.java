package com.funlisten.business.comment.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.album.model.bean.ZYComment;

import rx.Observable;

/**
 * Created by gd on 2017/7/25.
 */

public class ZYCommentModel extends ZYBaseModel {
    public Observable<ZYResponse<ZYListResponse<ZYComment>>> getComments(String type,String objectId,int pageIndex,int pageSize){
        return mApi.getComments(type,objectId,pageIndex,pageSize);
    }

    public Observable<ZYResponse<ZYComment>> addComment(String type,String objectId,String content){
        return mApi.addComment(type,objectId,content);
    }
}
