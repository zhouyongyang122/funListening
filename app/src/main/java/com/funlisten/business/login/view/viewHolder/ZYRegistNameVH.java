package com.funlisten.business.login.view.viewHolder;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.view.ZYClearEditView;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.login.model.bean.ZYRegistUpload;
import com.funlisten.business.login.view.ZYRegistListener;
import com.funlisten.utils.ZYToast;
import com.funlisten.utils.ZYUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/1.
 */

public class ZYRegistNameVH extends ZYBaseViewHolder<ZYRegistUpload> {

    @Bind(R.id.editName)
    ZYClearEditView editName;

    @Bind(R.id.textNext)
    TextView textNext;

    ZYRegistUpload mData;

    ZYRegistListener mListener;

    public ZYRegistNameVH(ZYRegistListener listener) {
        mListener = listener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        mItemView.setVisibility(View.GONE);
    }

    @Override
    public void updateView(ZYRegistUpload data, int position) {
        mData = data;
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String code = editName.getText().toString();
                if (!TextUtils.isEmpty(code)) {
                    textNext.setSelected(true);
                } else {
                    textNext.setSelected(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(!TextUtils.isEmpty(mData.nickname)){
            editName.setText(mData.nickname);
        }
    }

    public void attachTo(ViewGroup viewGroup) {
        bindView(LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResId(), viewGroup, false));
        viewGroup.addView(getItemView());
    }

    @OnClick({R.id.textNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textNext:
                String name = editName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ZYToast.show(mContext, "昵称不能为空!");
                    return;
                }
                mData.nickname = name;
                mListener.completeName();
                ZYUtils.hideInput(editName);
                break;
        }
    }

    public boolean isVisible(){
        return mItemView.getVisibility() == View.VISIBLE;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_regist_name;
    }

    public void show(){
        mItemView.setVisibility(View.VISIBLE);
    }

    public void hide(){
        mItemView.setVisibility(View.GONE);
    }
}
