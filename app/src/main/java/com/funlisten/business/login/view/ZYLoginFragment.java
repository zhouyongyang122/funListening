package com.funlisten.business.login.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.business.login.activity.ZYRegistActivity;
import com.funlisten.business.login.contract.ZYLoginContract;
import com.funlisten.business.login.model.bean.ZYThridLoginParamas;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.thirdParty.qq.TencentManager;
import com.funlisten.thirdParty.sina.SinaManager;
import com.funlisten.thirdParty.weChat.EventWeChatAuthor;
import com.funlisten.thirdParty.weChat.WeChatManager;
import com.funlisten.utils.ZYToast;
import com.funlisten.utils.ZYUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/6/30.
 */

public class ZYLoginFragment extends ZYBaseFragment<ZYLoginContract.IPresenter> implements ZYLoginContract.IView {

    @Bind(R.id.editMobile)
    EditText editMobile;

    @Bind(R.id.editPwd)
    EditText editPwd;

    ZYThridLoginParamas thridLoginParamas = new ZYThridLoginParamas();

    boolean thirdLoginComplete;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zy_activity_login, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void loginSuc(ZYUser user) {
        finish();
    }

    private void init() {

        WeChatManager.getInstance().initLogin(mActivity);

        SinaManager.getInstance().initLogin(mActivity);

        TencentManager.getInstance().initLogin(mActivity);
    }

    @OnClick({R.id.layoutWechat, R.id.layoutWeibo, R.id.layoutQQ, R.id.textLogin, R.id.textRegist, R.id.textForget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutWechat:
                if (!ZYUtils.isNetworkAvailable(mActivity)) {
                    ZYToast.show(mActivity, "网络不可用...");
                    return;
                }
                if (thirdLoginComplete) {
                    thirdLoginComplete = false;

                    if (WeChatManager.getInstance().getLoginApi() == null) {
                        thirdLoginComplete = true;
                        ZYToast.show(mActivity, "微信登录出错，请退出App,重新尝试");
                        return;
                    }

                    //微信没有安装
                    if (WeChatManager.getInstance().sendWeChatAuthRequest()) {
                        showProgress();
                    } else {
                        thirdLoginComplete = true;
                        ZYToast.show(mActivity, "没有安装微信App");
                    }

                } else {
                    ZYToast.show(mActivity, "请稍等,正在跳转第三方登录页面...");
                }
                break;
            case R.id.layoutWeibo:
                if (!ZYUtils.isNetworkAvailable(mActivity)) {
                    ZYToast.show(mActivity, "网络不可用...");
                    return;
                }
                if (thirdLoginComplete) {
                    thirdLoginComplete = false;

                    if (SinaManager.getInstance().getLoginHandler() == null) {
                        thirdLoginComplete = true;
                        ZYToast.show(mActivity, "微博登录出错，请退出App,重新尝试");
                        return;
                    }

                    SinaManager.getInstance().login(mActivity, new SinaManager.SinaLoginListener() {
                        @Override
                        public void onCancel(String msg) {
                            thirdLoginComplete = true;
                            ZYToast.show(mActivity, msg);
                        }

                        @Override
                        public void onError(String msg) {
                            thirdLoginComplete = true;
                            ZYToast.show(mActivity, msg);
                        }

                        @Override
                        public void onSuccess(final ZYThridLoginParamas loginParamas) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    thirdLoginComplete = true;
                                    mPresenter.loginByThrid(loginParamas.getParamas());
                                }
                            });
                        }
                    });

                } else {
                    ZYToast.show(mActivity, "请稍等,正在跳转第三方登录页面...");
                }

                break;
            case R.id.layoutQQ:
                if (!ZYUtils.isNetworkAvailable(mActivity)) {
                    ZYToast.show(mActivity, "网络不可用...");
                    return;
                }

                if (thirdLoginComplete) {
                    thirdLoginComplete = false;

                    if (TencentManager.getInstance().getmLoginTencent() == null) {
                        thirdLoginComplete = true;
                        ZYToast.show(mActivity, "QQ登录出错，请退出App,重新尝试");
                        return;
                    }

                    showProgress();
                    TencentManager.getInstance().login(mActivity, new TencentManager.TencentLoginListener() {
                        @Override
                        public void onCancel(String msg) {
                            thirdLoginComplete = true;
                            ZYToast.show(mActivity, msg);
                            hideProgress();
                        }

                        @Override
                        public void onError(String msg) {
                            thirdLoginComplete = true;
                            ZYToast.show(mActivity, msg);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(final ZYThridLoginParamas loginParamas) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    thirdLoginComplete = true;
                                    mPresenter.loginByThrid(loginParamas.getParamas());
                                }
                            });
                        }
                    });
                } else {
                    ZYToast.show(mActivity, "请稍等,正在跳转第三方登录页面...");
                }
                break;
            case R.id.textLogin: {
                String phone = editMobile.getText().toString();
                String pwd = editPwd.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ZYToast.show(mActivity, "手机号码不能为空!");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    ZYToast.show(mActivity, "密码不能为空!");
                    return;
                }
                mPresenter.login(phone, pwd);
            }
            break;
            case R.id.textRegist:
                startActivity(ZYRegistActivity.createIntent(mActivity));
                break;
            case R.id.textForget:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        thirdLoginComplete = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void weChatAuthResponse(EventWeChatAuthor weChatAuthor) {
        if (weChatAuthor.mUserInfo != null) {
            //成功
            thridLoginParamas.setToken(weChatAuthor.mUserInfo.openid);
            thridLoginParamas.setNickname(weChatAuthor.mUserInfo.nickname);
            thridLoginParamas.setSex(weChatAuthor.mUserInfo.sex);
            thridLoginParamas.setAuth_url(WeChatManager.getInstance().getAuth_url());
            thridLoginParamas.setAvatar(weChatAuthor.mUserInfo.headimgurl);
            thridLoginParamas.setType(ZYThridLoginParamas.TYPE_WECHAT);
            mPresenter.loginByThrid(thridLoginParamas.getParamas());
        } else {
            //失败
            thirdLoginComplete = true;
            hideProgress();
        }
    }
}
