package com.funlisten.business.accountmanage.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.utils.ZYToast;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/25.
 */

public class ZYSetPasswordActivity extends ZYBaseActivity {
    @Bind(R.id.pwd_one)
    EditText pwdOne;

    @Bind(R.id.pwd_two)
    EditText pwdTwo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gd_set_pwd_layout);
        showTitle("设置密码");
    }

    @OnClick({R.id.save_pwd})
    public void OnClick(View view){
        String pwd = pwdOne.getText().toString();
        String pwds = pwdTwo.getText().toString();
        if(TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwds)){
            ZYToast.show(this,"请输入密码");
            return;
        }
        if(!pwd.equals(pwds)){
            ZYToast.show(this,"前后密码不一致");
            return;
        }
    }
}
