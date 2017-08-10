package com.funlisten.service.net;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYCategory;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.order.ZYWeChatBack;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.favorite.ZYFavorite;
import com.funlisten.business.login.model.bean.ZYLoginUser;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.business.main.model.bean.ZYHome;
import com.funlisten.business.main.model.bean.ZYVersion;
import com.funlisten.business.order.ZYAlipayBack;
import com.funlisten.business.order.ZYOrder;
import com.funlisten.business.photo.ZYPhoto;
import com.funlisten.business.search.model.bean.ZYAudioAndAlbumInfo;
import com.funlisten.business.user.model.ZYProvince;
import com.funlisten.business.user.model.ZYUserList;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by ZY on 17/3/16.
 */

public interface ZYRequestApi {

    @GET("basic/getVersion")
    Observable<ZYResponse<ZYVersion>> getVersion();

    @POST("home/queryHomeData")
    Observable<ZYResponse<ZYHome>> getHomeData();

    @POST("category/list")
    Observable<ZYResponse<List<ZYCategory>>> getCategorys(@Query("level") int level);

    @POST("album/list")
    Observable<ZYResponse<ZYListResponse<ZYAlbumDetail>>> getAlbums(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("categoryId") int categoryId, @Query("publisherId") int publisherId);

    @POST("album/list")
    Observable<ZYResponse<ZYListResponse<ZYAlbumDetail>>> getAlbums(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("categoryId") int categoryId);

    @POST("album/detail")
    Observable<ZYResponse<ZYAlbumDetail>> getAlbumDetail(@Query("id") int id);

    @POST("sys/sendCode")
    Observable<ZYResponse> getCode(@Query("phone") String phone, @Query("type") String type);

    @POST("sys/checkCodeIsEquals")
    Observable<ZYResponse> checkCode(@Query("phone") String phone, @Query("type") String type, @Query("code") String code);

    @POST("user/regUser")
    Observable<ZYResponse> regUser(@QueryMap Map<String, String> paramas);

    @POST("user/doLogin")
    Observable<ZYResponse<ZYLoginUser>> login(@QueryMap Map<String, String> paramas);

    @POST("user/queryById")
    Observable<ZYResponse<ZYUser>> getUserInfo(@Query("userId") String userId);

    /**
     * 第三方绑定注册
     *
     * @return
     */
    @POST("/user/regWithOpenId")
    Observable<ZYResponse> regWithOpenId(@QueryMap Map<String, String> params);

    /**
     * 第三方id自动登录接口
     *
     * @return
     */
    @POST("user/autoLoginByOpenId")
    Observable<ZYResponse<ZYLoginUser>> autoLoginByOpenId(@QueryMap Map<String, String> params);

    /**
     * 更换手机绑定接口
     *
     * @param oldPhone
     * @param newPhone
     * @param code
     * @return
     */
    @POST("/user/updateBindPhone")
    Observable<ZYResponse> updateBindPhone(@Query("oldPhone") String oldPhone, @Query("newPhone") String newPhone, @Query("code") String code);

    /**
     * 找回密码
     */
    @POST("user/findPass")
    Observable<ZYResponse> findPass(@Query("phone") String phone, @Query("code") String code, @Query("password") String password);

    /**
     * 修改密码
     */
    @POST("user/updatePass")
    Observable<ZYResponse> updatePass(@Query("oldPass") String oldPass, @Query("newPass") String newPass);


    /**
     * oauth2获取accessToken
     *
     * @param code             第三方平台 跳转返回的code
     * @param openLoginChannel 第三方渠道(weChat/qq/weiBo)
     * @param redirectUrl      当渠道为qq和weibo需要传递此参数
     * @return
     */
    @POST("oauth/getAccessToken")
    Observable<ZYResponse> getAccessToken(@Query("code") String code, @Query("openLoginChannel") String openLoginChannel, @Query("redirectUrl") String redirectUrl);

    /**
     * 修改资料
     *
     * @param paramas (nickname,sex,
     *                ageRange(年龄范围。分别为00,90,80,70),
     *                areaCode(地区code=，跟用户信息查询接口一致，用‘|’隔开，areaCode与areaName成对修改),
     *                areaName(地区名称=，跟用户信息查询接口一致，用‘|’隔开),
     *                intro)
     * @return
     */
    @POST("user/updateUserDetail")
    Observable<ZYResponse> updateUserDetail(@QueryMap Map<String, String> paramas);

    /**
     * 更新头像
     */

    @POST("user/updateUserAvatar")
    @Multipart
    Observable<ZYResponse> updateUserAvatar(@Part MultipartBody.Part avatarFile);

//    Observable<ZYResponse> updateUserAvatar(@Field("avatarFile") byte[] avatarFile);


    /**
     * 评论
     *
     * @param type     评论对象类型；album：专辑，audio：单集
     * @param objectId 评论对象Id
     * @param content
     * @return
     */
    @POST("comment/insert")
    Observable<ZYResponse<ZYComment>> addComment(@Query("type") String type, @Query("objectId") String objectId, @Query("content") String content);

    /**
     * 评论列表
     *
     * @param type      评论对象类型；album：专辑，audio：单集
     * @param objectId  评论对象Id
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @POST("comment/list")
    Observable<ZYResponse<ZYListResponse<ZYComment>>> getComments(@Query("type") String type, @Query("objectId") String objectId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /**
     * 点赞
     *
     * @param objectId
     * @param type     点赞类型，comment：评论
     * @return
     */
    @POST("like")
    Observable<ZYResponse> suport(@Query("objectId") String objectId, @Query("type") String type);

    /**
     * 取消点赞
     *
     * @param objectId
     * @param type     点赞类型，comment：评论
     * @return
     */
    @POST("like/cancel")
    Observable<ZYResponse> suportCanlce(@Query("objectId") String objectId, @Query("type") String type);

    /**
     * 收藏
     *
     * @param objectId
     * @param type     喜欢、订阅类型，album：专辑，audio：单集
     * @return
     */
    @POST("favorite")
    Observable<ZYResponse> favorite(@Query("objectId") String objectId, @Query("type") String type);

    /**
     * 取消收藏
     *
     * @param objectId
     * @param type     喜欢、订阅类型，album：专辑，audio：单集
     * @return
     */
    @POST("favorite/cancel")
    Observable<ZYResponse> favoriteCancel(@Query("objectId") String objectId, @Query("type") String type);


    /**
     * 订阅(专辑列表) 收藏(音频)列表
     *
     * @param type      album：专辑，audio：单集
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @POST("favorite/list")
    Observable<ZYResponse<ZYListResponse<ZYFavorite>>> getFavorites(@Query("type") String type, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /**
     * 我的订购列表
     *
     * @param type      album：专辑，audio：单集
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @POST("user/albumAudio")
    Observable<ZYResponse<ZYListResponse<ZYOrder>>> getorders(@Query("type") String type, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /***
     * 是否已经订购
     * @param  type  album：专辑，audio：单集
     * @param  objectId 对应的专辑id或音频id
     * */
    @POST("user/checkAlbumAudio")
    Observable<ZYResponse<Boolean>> isOrder(@Query("type") String type, @Query("objectId") String objectId);

    /**
     * 支付宝下单
     *
     * @param productDetail (单个专辑：productDetail=[{"productId":"7","productType":"album"}]
     *                      多个音频：productDetail=[{"productId":"9","productType":"audio"},{"productId":"10","productType":"audio"}]
     *                      productId:音频或专辑id
     *                      productType:album-专辑,audio-音频)
     */
    @FormUrlEncoded
    @POST("order/alipay/appTrade")
    Observable<ZYResponse<ZYAlipayBack>> alipay(@Field("productDetail") String productDetail);

    /****
     * 微信下单
     * ****/
    @FormUrlEncoded
    @POST("order/weChat/unifiedOrder")
    Observable<ZYResponse<ZYWeChatBack>> wxPay(@Field("productDetail") String productDetail);

    /**
     * 音频列表
     *
     * @param pageIndex
     * @param pageSize
     * @param albumId
     * @param direction 排序规则，desc：倒序，asc：正序
     * @return
     */
    @POST("audio/list")
    Observable<ZYResponse<ZYListResponse<ZYAudio>>> getAudios(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("albumId") int albumId, @Query("direction") String direction);


    /**
     * 获取音频对象url
     *
     * @param id
     * @return
     */
    @POST("audio/queryDownloadInfo")
    Observable<ZYResponse> getAudioUrl(@Query("id") String id);

    /**
     * 获取音频对象
     *
     * @param id
     * @return
     */
    @POST("audio/detail")
    Observable<ZYResponse<ZYAudio>> getAudio(@Query("id") String id);

    /**
     * 关注
     */
    @POST("user/follow")
    Observable<ZYResponse> follow(@Query("toUserId") String toUserId);

    /**
     * 关注
     */
    @POST("user/unfollow")
    Observable<ZYResponse> followCancle(@Query("toUserId") String toUserId);

    /**
     * 关注列表
     */
    @POST("user/follower")
    Observable<ZYResponse<ZYListResponse<ZYUserList>>> follows(@Query("userId") String userId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /**
     * 粉丝列表
     */
    @POST("user/fan")
    Observable<ZYResponse<ZYListResponse<ZYUserList>>> fans(@Query("userId") String userId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /**
     * 相册列表
     */
    @POST("user/photo/list")
    Observable<ZYResponse<ZYListResponse<ZYPhoto>>> photos(@Query("userId") String userId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /**
     * 添加照片
     */
    @POST("user/photo/insert")
    @Multipart
    Observable<ZYResponse<ZYPhoto>> addPhoto(@Part MultipartBody.Part photo);

    /**
     * 删除照片
     *
     * @param ids 照片id 多个用,号隔开
     */
    @POST("user/photo/delete")
    Observable<ZYResponse> delPhoto(@Query("ids") String ids);

    /**
     * 搜索热词
     */
    @POST("search/querySearchHotWords")
    Observable<ZYResponse<List<String>>> getSearchWords();

    /***
     * 搜索
     * **/
    @POST("search/albumAudio")
    Observable<ZYResponse<ZYListResponse<ZYAudioAndAlbumInfo>>> getSearch(@Query("type") String type, @Query("keyword") String keyword);

    /**
     * 省市信息接口
     */
    @POST("province/listAll")
    Observable<ZYResponse<List<ZYProvince>>> getCities();

    /**
     * 是否订阅查询
     *
     * @param type album：专辑，audio：单集
     */
    @POST("favorite/isFavorite")
    Observable<ZYResponse<Boolean>> isFavorite(@Query("type") String type, @Query("objectId") int objectId);

    /**
     * 是否订阅查询
     * 关注状态（no_follow：未关注，following：关注中，be_follow：被关注，mutually_followed：互相关注）
     */
    @POST("user/followStatus")
    Observable<ZYResponse<String>> isFollowStatus(@Query("toUserId") int toUserId);

    @POST("audio/addPlayCount")
    Observable<ZYResponse> reportPlay(@Query("id") int audioId);

    @POST("audio/addDownloadCount")
    Observable<ZYResponse> reportDownload(@Query("id") int audioId);
}
