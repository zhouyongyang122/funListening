package com.funlisten.business.play.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.play.model.bean.ZYAudio;

import rx.Observable;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayModel extends ZYBaseModel {

    /**
     * 获取音频对象url
     *
     * @param id
     * @return
     */
    Observable<ZYResponse> getAudioUrl(String id) {
        return mApi.getAudioUrl(id);
    }

    /**
     * 获取音频对象
     *
     * @param id
     * @return
     */
    public Observable<ZYResponse<ZYAudio>> getAudio(String id) {
        return mApi.getAudio(id);
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
    public Observable<ZYResponse<ZYListResponse<ZYComment>>> getComments(String type,String objectId, int pageIndex, int pageSize) {
        return mApi.getComments(type, objectId, pageIndex, pageSize);
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
