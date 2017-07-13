package com.funlisten.business.play.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
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
    Observable<ZYResponse> getAudio(String id) {
        return mApi.getAudio(id);
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
