package com.funlisten.business.login.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.base.view.ZYRegistDialog;
import com.funlisten.business.login.contract.ZYRegistContract;
import com.funlisten.business.login.model.bean.ZYRegistUpload;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.business.login.view.viewHolder.ZYRegistCodeVH;
import com.funlisten.business.login.view.viewHolder.ZYRegistGenderVH;
import com.funlisten.business.login.view.viewHolder.ZYRegistMobileVH;
import com.funlisten.business.login.view.viewHolder.ZYRegistNameVH;
import com.funlisten.utils.ZYToast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/7/1.
 */

public class ZYRegistFragment extends ZYBaseFragment<ZYRegistContract.IPresenter> implements ZYRegistContract.IView, ZYRegistListener,ZYRegistDialog.CancelAndFinishListener {

    @Bind(R.id.rootLayout)
    RelativeLayout rootLayout;

    ZYRegistMobileVH mobileVH;

    ZYRegistCodeVH codeVH;

    ZYRegistNameVH nameVH;

    ZYRegistGenderVH genderVH;

    ZYRegistUpload mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zy_activity_regist, container, false);
        ButterKnife.bind(this, view);
        mData = new ZYRegistUpload();

        mobileVH = new ZYRegistMobileVH(this);
        mobileVH.attachTo(rootLayout);
        mobileVH.updateView(mData, 0);

        codeVH = new ZYRegistCodeVH(this);
        codeVH.attachTo(rootLayout);
        codeVH.updateView(mData, 0);

        nameVH = new ZYRegistNameVH(this);
        nameVH.attachTo(rootLayout);
        nameVH.updateView(mData, 0);

        genderVH = new ZYRegistGenderVH(this);
        genderVH.attachTo(rootLayout);
        genderVH.updateView(mData, 0);

        return view;
    }

    @Override
    public void checkPhoneIsExists() {
        mPresenter.checkPhoneIsExists(mData.phone);
    }

    @Override
    public void completeMobile() {
        codeVH.show();
    }

    @Override
    public void completeCode() {
        nameVH.show();
    }

    @Override
    public void completeName() {
        genderVH.show();
    }

    @Override
    public void completeGender() {
        //发送注册请求
        mPresenter.regUser(mData.getParams());
    }

    @Override
    public void isShowPwd(boolean isShow) {
        if(isShow)
        mobileVH.isShowPwd(isShow);
        else {
            new ZYRegistDialog(mActivity,this).show();
        }
    }

    @Override
    public void onCancel() {
        mobileVH.isShowPwd(false);
    }

    @Override
    public void onFinish() {
        mActivity.finish();
    }

    @Override
    public void sendCode() {
        mPresenter.sendCode(mData.phone);
    }

    @Override
    public void codeSuc() {
        codeVH.codeSuc();
    }

    @Override
    public void registSuc(ZYUser user) {
        ZYToast.show(mActivity, "注册成功!");
        finish();
    }

    public boolean onBackPressed() {
        if (genderVH.isVisible()) {
            genderVH.hide();
            return false;
        }

        if (nameVH.isVisible()) {
            nameVH.hide();
            return false;
        }

        if (codeVH.isVisible()) {
            codeVH.hide();
            return false;
        }
        return true;
    }
}
