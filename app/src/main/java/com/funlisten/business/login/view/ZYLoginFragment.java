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
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.utils.ZYToast;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zy_activity_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void loginSuc(ZYUser user) {
        finish();
    }

    @OnClick({R.id.layoutWechat, R.id.layoutWeibo, R.id.layoutQQ, R.id.textLogin, R.id.textRegist, R.id.textForget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutWechat:
                break;
            case R.id.layoutWeibo:
                break;
            case R.id.layoutQQ:
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

}
