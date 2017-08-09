package com.funlisten.business.accountmanage.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.business.accountmanage.contract.ZYModifyPwd;
import com.funlisten.business.accountmanage.model.ZYAccountManageModel;
import com.funlisten.business.accountmanage.presenter.ZYAccountManagerPresenter;
import com.funlisten.utils.ZYToast;
import com.funlisten.utils.ZYUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/25.
 */

public class ZYModifyPwdActivity extends ZYBaseActivity implements ZYModifyPwd {

    @Bind(R.id.pwd_old)
    EditText pwdOld;

    @Bind(R.id.pwd_one)
    EditText pwdOne;

    @Bind(R.id.pwd_two)
    EditText pwdTwo;

    ZYAccountManagerPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gd_modify_pwd_layout);
        showTitle("修改密码");
        presenter = new ZYAccountManagerPresenter(new ZYAccountManageModel());
        presenter.setModifyPwd(this);
    }

    @OnClick({R.id.save_pwd})
    public void OnClick(View view) {
        String old = pwdOld.getText().toString();
        String pwd = pwdOne.getText().toString();
        String pwds = pwdTwo.getText().toString();

        if (TextUtils.isEmpty(old)) {
            ZYToast.show(mActivity, "旧密码不能为空");
            return;
        }

        if (!ZYUtils.checkPassword(mActivity, pwd)) {
            return;
        }

        if (!pwd.equals(pwds)) {
            ZYToast.show(this, "密码不一致");
            return;
        }
        presenter.updatePass(old, pwd);
    }

    @Override
    public void onSuccess() {
        finish();
    }

    @Override
    public void onFail() {

    }
}
