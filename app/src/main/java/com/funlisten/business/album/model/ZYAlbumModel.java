package com.funlisten.business.album.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYCategory;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.play.model.bean.ZYAudio;

import java.util.List;

import rx.Observable;

/**
 * Created by ZY on 17/6/13.
 */

public class ZYAlbumModel extends ZYBaseModel {

    public static final String SORT_DESC = "desc";//倒序
    public static final String SORT_ASC = "asc";//正序

    public Observable<ZYResponse<List<ZYCategory>>> getCategorys() {
        return mApi.getCategorys(0);
    }

    public Observable<ZYResponse<ZYListResponse<ZYAlbumDetail>>> getAblums(int pageIndex, int pageSize, int categoryId, int publisherId) {
        return mApi.getAlbums(pageIndex, pageSize, categoryId, publisherId);
    }

    public Observable<ZYResponse<ZYListResponse<ZYAlbumDetail>>> getAblums(int pageIndex, int pageSize, int categoryId) {
        return mApi.getAlbums(pageIndex, pageSize, categoryId);
    }

    public Observable<ZYResponse<ZYAlbumDetail>> getAlbumDetail(int id) {
        return mApi.getAlbumDetail(id);
    }



    /**
     * 评论列表
     * <p>
     * 评论对象类型；album：专辑，audio：单集
     *
     * @param objectId  评论对象Id
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public Observable<ZYResponse<ZYListResponse<ZYComment>>> getComments(String objectId, int pageIndex, int pageSize) {
        return mApi.getComments("album", objectId, pageIndex, pageSize);
    }

    /**
     * 音频列表
     *
     * @param pageIndex
     * @param pageSize
     * @param albumId
     * @param direction 排序规则，desc：倒序，asc：正序
     * @return
     */
    public Observable<ZYResponse<ZYListResponse<ZYAudio>>> getAudios(int pageIndex, int pageSize, int albumId, String direction) {
        return mApi.getAudios(pageIndex, pageSize, albumId, direction);
    }
}
