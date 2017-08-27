package com.funlisten.business.login.view.viewHolder;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.view.ZYClearEditView;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.login.model.bean.ZYRegistUpload;
import com.funlisten.business.login.view.ZYRegistListener;
import com.funlisten.business.set.activity.ZYAgreementActivity;
import com.funlisten.utils.ZYToast;
import com.funlisten.utils.ZYUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/1.
 */

public class ZYRegistMobileVH extends ZYBaseViewHolder<ZYRegistUpload> implements TextWatcher {

    @Bind(R.id.editMobile)
    ZYClearEditView editMobile;

    @Bind(R.id.editPwd)
    ZYClearEditView editPwd;

    @Bind(R.id.checkProtocol)
    CheckBox checkProtocol;

    @Bind(R.id.textNext)
    TextView textNext;

    ZYRegistUpload mData;

    ZYRegistListener mListener;

    public ZYRegistMobileVH(ZYRegistListener listener) {
        mListener = listener;
    }

    @Override
    public void updateView(ZYRegistUpload data, int position) {
        mData = data;
        editMobile.addTextChangedListener(this);
        editPwd.addTextChangedListener(this);
    }

    public void attachTo(ViewGroup viewGroup) {
        bindView(LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResId(), viewGroup, false));
        viewGroup.addView(getItemView());
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_regist_mobile;
    }

    @OnClick({R.id.textNext, R.id.textProtocol})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textNext:
                String mobile = editMobile.getText().toString();
                String pwd = editPwd.getText().toString();
                if (!mobile.startsWith("1") || mobile.length() != 11) {
                    ZYToast.show(mContext, "手机号码不正确!");
                    return;
                }
                mData.phone = mobile;
                if (editPwd.getVisibility() == View.GONE) {
                    mListener.checkPhoneIsExists();
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    ZYToast.show(mContext, "密码不能为空!");
                    return;
                }

                mData.password = pwd;
                mListener.completeMobile();

                ZYUtils.hideInput(editMobile);
                break;
            case R.id.textProtocol:
                mContext.startActivity(new Intent(mContext, ZYAgreementActivity.class));
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String mobile = editMobile.getText().toString();
        String pwd = editPwd.getText().toString();
        if (!TextUtils.isEmpty(pwd) && mobile.startsWith("1") && mobile.length() == 11) {
            textNext.setSelected(true);
        } else {
            textNext.setSelected(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void isShowPwd(boolean isShow) {
        if (isShow) {
            editPwd.setVisibility(View.VISIBLE);
        } else editPwd.setVisibility(View.GONE);
    }

    public void show() {
        mItemView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mItemView.setVisibility(View.GONE);
    }
}
