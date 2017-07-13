package com.funlisten.business.login.view.viewHolder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.login.model.bean.ZYRegistUpload;
import com.funlisten.business.login.view.ZYRegistListener;
import com.funlisten.utils.ZYToast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/1.
 */

public class ZYRegistGenderVH extends ZYBaseViewHolder<ZYRegistUpload> {

    @Bind(R.id.layoutMan)
    RelativeLayout layoutMan;
    @Bind(R.id.imgManCheck)
    ImageView imgManCheck;

    @Bind(R.id.layoutWoman)
    RelativeLayout layoutWoman;
    @Bind(R.id.imgWomanCheck)
    ImageView imgWomanCheck;

    @Bind(R.id.textAge1)
    TextView textAge1;

    @Bind(R.id.textAge2)
    TextView textAge2;

    @Bind(R.id.textAge3)
    TextView textAge3;

    @Bind(R.id.textAge4)
    TextView textAge4;

    ArrayList<TextView> ageViews = new ArrayList<TextView>();

    ZYRegistUpload mData;

    ZYRegistListener mListener;

    public ZYRegistGenderVH(ZYRegistListener listener) {
        mListener = listener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        ageViews.add(textAge1);
        ageViews.add(textAge2);
        ageViews.add(textAge3);
        ageViews.add(textAge4);
        mItemView.setVisibility(View.GONE);
    }

    @Override
    public void updateView(ZYRegistUpload data, int position) {
        mData = data;

        if(!TextUtils.isEmpty(mData.ageRange)){
            if(mData.ageRange.equals("00")){
                selectedAge(0);
            }else if(mData.ageRange.equals("90")){
                selectedAge(1);
            }else if(mData.ageRange.equals("80")){
                selectedAge(2);
            }else if(mData.ageRange.equals("70")){
                selectedAge(3);
            }
        }

        if(!TextUtils.isEmpty(mData.sex)){
            if(mData.sex.equals("male")){
                imgManCheck.setVisibility(View.VISIBLE);
                imgWomanCheck.setVisibility(View.GONE);
            }else {
                imgManCheck.setVisibility(View.GONE);
                imgWomanCheck.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.layoutMan,R.id.layoutWoman,R.id.textOk, R.id.textAge1, R.id.textAge2, R.id.textAge3, R.id.textAge4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textOk:
                if(TextUtils.isEmpty(mData.sex)){
                    ZYToast.show(mContext,"还没有选择性别!");
                    return;
                }
                if(TextUtils.isEmpty(mData.ageRange)){
                    ZYToast.show(mContext,"还没有选择年龄段!");
                    return;
                }
                mListener.completeGender();
                break;
            case R.id.textAge1:
                selectedAge(0);
                mData.ageRange = "00";
                break;
            case R.id.textAge2:
                selectedAge(1);
                mData.ageRange = "90";
                break;
            case R.id.textAge3:
                selectedAge(2);
                mData.ageRange = "80";
                break;
            case R.id.textAge4:
                selectedAge(3);
                mData.ageRange = "70";
                break;
            case R.id.layoutMan:
                imgManCheck.setVisibility(View.VISIBLE);
                imgWomanCheck.setVisibility(View.GONE);
                mData.sex = "male";
                break;
            case R.id.layoutWoman:
                imgWomanCheck.setVisibility(View.VISIBLE);
                imgManCheck.setVisibility(View.GONE);
                mData.sex = "female";
                break;
        }
    }

    private void selectedAge(int position) {
        for (int i = 0; i < ageViews.size(); i++) {
            if (position == i) {
                ageViews.get(i).setSelected(true);
            } else {
                ageViews.get(i).setSelected(false);
            }
        }
    }

    public void attachTo(ViewGroup viewGroup) {
        bindView(LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResId(), viewGroup, false));
        viewGroup.addView(getItemView());
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_regist_gender;
    }

    public void show(){
        mItemView.setVisibility(View.VISIBLE);
    }

    public boolean isVisible(){
        return mItemView.getVisibility() == View.VISIBLE;
    }

    public void hide(){
        mItemView.setVisibility(View.GONE);
    }
}
