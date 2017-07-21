package com.funlisten.business.login.model;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.login.model.bean.ZYLoginUser;
import com.funlisten.business.login.model.bean.ZYUser;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ZY on 17/6/30.
 */

public class ZYLoginModel extends ZYBaseModel {

    //用户注册验证码
    public static final String TYPE_REGIST_CODE = "userRegister";

    //找回密码验证码
    public static final String TYPE_FIND_PASS_CODE = "findPass";

    public Observable<ZYResponse> getCode(String phone, String type) {
        return mApi.getCode(phone, type);
    }

    public Observable<ZYResponse> regUser(Map<String, String> paramas) {
        return mApi.regUser(paramas);
    }

    public Observable<ZYResponse<ZYLoginUser>> login(String phone, String password) {
        Map<String, String> paramas = new HashMap<String, String>();
        paramas.put("phone", phone);
        paramas.put("password", password);
        return mApi.login(paramas);
    }

    public Observable<ZYResponse<ZYUser>> getUserInfo(String userId) {
        return mApi.getUserInfo(userId);
    }

    /**
     * 第三方绑定注册
     *
     * @return
     */
    public Observable<ZYResponse> regWithOpenId(Map<String, String> params) {
        return mApi.regWithOpenId(params);
    }

    /**
     * 第三方id自动登录接口
     *
     * @return
     */
    public Observable<ZYResponse<ZYLoginUser>> autoLoginByOpenId(Map<String, String> params) {
        return mApi.autoLoginByOpenId(params);
    }
}
