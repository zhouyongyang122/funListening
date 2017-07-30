package com.funlisten.business.accountmanage.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.business.accountmanage.contract.ZYAccountManagerContract;
import com.funlisten.business.accountmanage.model.ZYAccountManageModel;
import com.funlisten.business.accountmanage.presenter.ZYAccountManagerPresenter;
import com.funlisten.utils.ZYToast;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/25.
 */

public class ZYForgotPasswordActivity extends ZYBaseActivity implements ZYAccountManagerContract.View {

    @Bind(R.id.verify_time)
    TextView verifyTime;

    @Bind(R.id.bind_tv)
    TextView bindTv;

    @Bind(R.id.phone_edit)
    EditText phoneNum;

    @Bind(R.id.verify_code)
    EditText verifyCode;

    @Bind(R.id.pwd_one)
    EditText pwdOne;

    @Bind(R.id.pwd_two)
    EditText pwdTwo;

    ZYAccountManagerPresenter presenter;

    String phone;

    String code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gd_forgot_pwd_layout);
        presenter = new ZYAccountManagerPresenter(new ZYAccountManageModel(),this);
        bindTv.setEnabled(false);
        showTitle("找回密码");
    }
    @OnClick({R.id.verify_time,R.id.bind_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.verify_time:
                phone = phoneNum.getText().toString();
                if(TextUtils.isEmpty(phone) || phone.length() >11){
                    ZYToast.show(this,"请输入正确的手机号!");
                    break;
                }
                presenter.getCode(phone,"findPass");
                restart();
                verifyTime.setEnabled(false);
                verifyTime.setBackgroundResource(R.color.c4);
                bindTv.setEnabled(true);
                bindTv.setBackgroundResource(R.color.c1);
                phoneNum.setEnabled(false);
                break;
            case R.id.bind_tv:
                code  = verifyCode.getText().toString();
                if(TextUtils.isEmpty(code) ){
                    ZYToast.show(this,"请输入验证码!");
                    break;
                }
                String onePwd = pwdOne.getText().toString();
                String twoPwd = pwdTwo.getText().toString();

                if(TextUtils.isEmpty(onePwd) && TextUtils.isEmpty(twoPwd)){
                    ZYToast.show(this,"请输入密码!");
                    break;
                }

                if(!onePwd.equals(twoPwd)){
                    ZYToast.show(this,"密码不一致!");
                    break;
                }
                presenter.findPass(phone,code,onePwd);
                break;
        }
    }

    public void oncancel() {
        timer.cancel();
    }

    public void restart() {
        timer.start();
    }

    private CountDownTimer timer = new CountDownTimer(1000*60, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            verifyTime.setText((millisUntilFinished / 1000) + "秒后可重发");
        }

        @Override
        public void onFinish() {
            verifyTime.setEnabled(true);
            verifyTime.setText("验证");
            verifyTime.setBackgroundResource(R.color.c1);
        }
    };

    @Override
    public void sendCodeFail() {
        oncancel();
        verifyTime.setEnabled(true);
        verifyTime.setBackgroundResource(R.color.c1);
        verifyTime.setText("验证");
        bindTv.setEnabled(false);
        bindTv.setBackgroundResource(R.color.c4);
        phoneNum.setEnabled(true);
    }

    @Override
    public void checkCodeSuccces() {
        oncancel();
        finish();
    }
}
