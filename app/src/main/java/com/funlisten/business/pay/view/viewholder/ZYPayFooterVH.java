package com.funlisten.business.pay.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/29.
 */

public class ZYPayFooterVH extends ZYBaseViewHolder {

    @Bind(R.id.ali_check)
    ImageView aliCheck;

    @Bind(R.id.wechat_check)
    ImageView wechat_check;

    PayListener payListener;

    int payType =1;

    public ZYPayFooterVH(PayListener payListener) {
        this.payListener = payListener;
    }

    @Override
    public void updateView(Object data, int position) {
        aliCheck.setSelected(true);
    }

    @OnClick({R.id.ali_check,R.id.wechat_check,R.id.finish_pay})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.ali_check:
                clearSelect(false);
                aliCheck.setSelected(true);
                payType = 1;
                break;
            case R.id.wechat_check:
                clearSelect(false);
                wechat_check.setSelected(true);
                payType = 2;
                break;
            case R.id.finish_pay:
                payListener.onPay(payType);
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_pay_ft_item;
    }

    private void clearSelect(boolean isSelect){
        aliCheck.setSelected(isSelect);
        wechat_check.setSelected(isSelect);
    }

    public interface PayListener{
        void onPay(int payType);
    }
}

