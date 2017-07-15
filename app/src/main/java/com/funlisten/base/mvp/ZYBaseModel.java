package com.funlisten.base.mvp;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.html5.ZYHtml5UrlBean;
import com.funlisten.service.net.ZYNetManager;
import com.funlisten.service.net.ZYRequestApi;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ZY on 17/3/14.
 */

public class ZYBaseModel {

    protected ZYRequestApi mApi;

    public static final String ALBUM_TYPE = "album";

    public static final String AUDIO_TYPE = "audio";

    //没有关注
    public static final String FOLLOW_NO_STATE = "no_follow";

    //关注中
    public static final String FOLLOW_HAS_STATE = "following";

    //被关注
    public static final String FOLLOW_BE_STATE = "be_follow";

    //互相关注
    public static final String FOLLOW_MUTUALLY_STATE = "mutually_followed";

    public static final String COMMENT_TYPE = "comment";

    public ZYBaseModel() {
        mApi = ZYNetManager.shareInstance().getApi();
    }

//    public Observable<ZYResponse<ZYHtml5UrlBean>> getHtml5Urls() {
//        return mApi.getHtml5Urls();
//    }

    /**
     * 是否订阅查询
     *
     * @param type album：专辑，audio：单集
     */
    public Observable<ZYResponse<Boolean>> isFavorite(String type, int objectId) {
        return mApi.isFavorite(type, objectId);
    }

    /**
     * 是否订阅查询
     * 关注状态（no_follow：未关注，following：关注中，be_follow：被关注，mutually_followed：互相关注）
     */
    public Observable<ZYResponse<String>> isFollowStatus(int toUserId) {
        return mApi.isFollowStatus(toUserId);
    }

    /**
     * 点赞
     *
     * @param objectId
     * @param type     点赞类型，comment：评论
     * @return
     */
    public Observable<ZYResponse> suport(String objectId, String type) {
        return mApi.suport(objectId, type);
    }

    /**
     * 取消点赞
     *
     * @param objectId
     * @param type     点赞类型，comment：评论
     * @return
     */
    public Observable<ZYResponse> suportCanlce(@Query("objectId") String objectId, @Query("type") String type) {
        return mApi.suportCanlce(objectId, type);
    }

    /**
     * 收藏
     *
     * @param objectId
     * @param type     喜欢、订阅类型，album：专辑，audio：单集
     * @return
     */
    public Observable<ZYResponse> favorite(String objectId, String type) {
        return mApi.favorite(objectId, type);
    }

    /**
     * 取消收藏
     *
     * @param objectId
     * @param type     喜欢、订阅类型，album：专辑，audio：单集
     * @return
     */
    public Observable<ZYResponse> favoriteCancel(String objectId, String type) {
        return mApi.favoriteCancel(objectId, type);
    }

    /**
     * 关注
     */
    public Observable<ZYResponse> follow(String toUserId){
        return mApi.follow(toUserId);
    }

    /**
     * 关注
     */
    public Observable<ZYResponse> followCancle(String toUserId){
        return mApi.followCancle(toUserId);
    }

}
