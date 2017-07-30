package com.funlisten.business.pay.model;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.order.ZYAlipayBack;
import com.funlisten.business.order.ZYWeChatBack;

import rx.Observable;

/**
 * Created by gd on 2017/7/28.
 */

public class ZYPayModel extends ZYBaseModel {
    public Observable<ZYResponse<ZYAlipayBack>> alipay(String productDetail){
        return mApi.alipay(productDetail);
    }

    public  Observable<ZYResponse<ZYWeChatBack>> wxPay(String productDetail){
        return  mApi.wxPay(productDetail);
    }

}
