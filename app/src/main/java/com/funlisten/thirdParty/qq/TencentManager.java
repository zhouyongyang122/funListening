package com.funlisten.thirdParty.qq;

import android.app.Activity;

import com.funlisten.ZYAppConstants;
import com.funlisten.ZYApplication;
import com.funlisten.business.login.model.bean.ZYThridLoginParamas;
import com.funlisten.utils.ZYLog;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by ZY on 17/4/14.
 */

public class TencentManager {

    private static TencentManager instance;

    private Tencent mLoginTencent;

    private TencentLoginUiListener uiListener;

    private TencentLoginListener mLoginListener;

    private TencentManager() {

    }

    public static TencentManager getInstance() {
        if (instance == null) {
            instance = new TencentManager();
        }
        return instance;
    }

    public void initLogin(Activity activity) {
        try {
            mLoginTencent = Tencent.createInstance(ZYAppConstants.TENCENT_APP_ID,
                    activity.getApplicationContext());
        } catch (Exception e) {
            mLoginTencent = null;
        }
    }

    public void login(final Activity activity, TencentLoginListener loginListener) {
        uiListener = new TencentLoginUiListener();
        mLoginListener = loginListener;
        mLoginTencent.login(activity, "all", uiListener);
    }

    public Tencent getmLoginTencent() {
        return mLoginTencent;
    }

    class TencentLoginUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {

            final ZYThridLoginParamas loginParamas = new ZYThridLoginParamas();
            String token = null;
            String openId = null;
            try {
                token = ((JSONObject) response).getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                openId = ((JSONObject) response).getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
                loginParamas.setType(ZYThridLoginParamas.TYPE_TENCENT);
                loginParamas.setToken(openId);

                QQToken qqToken = mLoginTencent.getQQToken();
                UserInfo info = new UserInfo(ZYApplication.getInstance().getCurrentActivity(), qqToken);
                String auth_url = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=" + qqToken.getAppId()
                        + "&access_token=" + token +
                        "&openid=" + openId + "&format=json";

                loginParamas.setAuth_url(auth_url);

                info.getUserInfo(new IUiListener() {
                    public void onComplete(final Object response) {
                        JSONObject json = (JSONObject) response;
                        // 获取用户信息
                        try {
                            loginParamas.setAvatar(json.getString("figureurl_qq_2"));
                            loginParamas.setNickname(json.getString("nickname"));
                            loginParamas.setSex(json.getString("gender").equals("男") ? 1 : 2);
                            if (mLoginListener != null) {
                                mLoginListener.onSuccess(loginParamas);
                            }
                        } catch (Exception e) {
                            ZYLog.e(TencentManager.class.getSimpleName(), "login-erro: " + e.getMessage());
                            if (mLoginListener != null) {
                                mLoginListener.onError("QQ登录失败,请重新尝试!");
                            }
                        }
                    }

                    public void onCancel() {
                        if (mLoginListener != null) {
                            mLoginListener.onCancel("QQ登录取消");
                        }
                    }

                    public void onError(UiError uiError) {
                        ZYLog.e(TencentManager.class.getSimpleName(), "login-erro: " + uiError.errorDetail);
                        if (mLoginListener != null) {
                            mLoginListener.onError("QQ登录失败,请重新尝试!");
                        }
                    }

                });

            } catch (Exception e) {
                ZYLog.e(TencentManager.class.getSimpleName(), "login-erro: " + e.getMessage());
                if (mLoginListener != null) {
                    mLoginListener.onError("QQ登录失败,请重新尝试!");
                }
                return;
            }
        }

        @Override
        public void onError(UiError uiError) {
            ZYLog.e(TencentManager.class.getSimpleName(), "login-erro: " + uiError.errorDetail);
            if (mLoginListener != null) {
                mLoginListener.onError("QQ登录失败,请重新尝试!");
            }
        }

        @Override
        public void onCancel() {
            if (mLoginListener != null) {
                mLoginListener.onCancel("QQ登录取消");
            }
        }
    }

    public interface TencentLoginListener {
        void onCancel(String msg);

        void onError(String msg);

        void onSuccess(ZYThridLoginParamas loginParamas);
    }
}
