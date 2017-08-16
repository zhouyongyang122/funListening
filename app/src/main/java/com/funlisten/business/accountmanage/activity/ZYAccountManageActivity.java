package com.funlisten.business.accountmanage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.business.login.model.bean.ZYUser;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/24.
 */

public class ZYAccountManageActivity extends ZYBaseActivity {
    @Bind(R.id.phone)
    TextView phone;

    ZYUser zyUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gd_account_manage_layout);
        showTitle("账号管理");
        zyUser = ZYUserManager.getInstance().getUser();
        initView();
    }
    private  void initView(){
        phone.setText(TextUtils.isEmpty(zyUser.phone) ? "":zyUser.phone);
    }

    @OnClick({R.id.phone_line,R.id.password_line})
    public  void OnClick(View view){
        switch (view.getId()){
            case R.id.phone_line:
                startActivity(new Intent(this,ZYMobileBindActivity.class));
                break;
            case R.id.password_line:
                startActivity(new Intent(this,ZYModifyPwdActivity.class));
                break;
        }
    }

}
