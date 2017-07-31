package com.funlisten.business.pay.presenter;

import android.app.Activity;

import com.alipay.sdk.pay.AlipayUtils;
import com.funlisten.ZYApplication;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.business.order.ZYAlipayBack;
import com.funlisten.business.order.ZYWeChatBack;
import com.funlisten.business.pay.contract.ZYPayContract;
import com.funlisten.business.pay.model.ZYPayModel;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.utils.WeChatPayUtils;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYToast;
import com.funlisten.wxapi.WeChaPayManager;
import com.funlisten.wxapi.WeChatPayCallBack;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/29.
 */

public class ZYPayPresenter extends ZYBasePresenter implements ZYPayContract.Presenter {
    ZYPayModel mModel;
    ZYPayContract.View mView;

    public ZYPayPresenter(ZYPayModel mModel, ZYPayContract.View mView) {
        this.mModel = mModel;
        this.mView = mView;
    }

    @Override
    public void getSignALiAudio(String json){
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.alipay(json),new ZYNetSubscriber<ZYResponse<ZYAlipayBack>>(){

            @Override
            public void onSuccess(ZYResponse<ZYAlipayBack> response) {
                super.onSuccess(response);
                goAliPay(response.data);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                mView.onPayFaild();
            }
        }));
    }

    @Override
    public void getWeChatSign(String productDetail) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.wxPay(productDetail),new ZYNetSubscriber<ZYResponse<ZYWeChatBack>>(){

            @Override
            public void onSuccess(ZYResponse<ZYWeChatBack> response) {
                super.onSuccess(response);
                goWeChatPay(response.data);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                mView.onPayFaild();
            }
        }));
    }

    private void  goAliPay(ZYAlipayBack back){
        AlipayUtils.getInStance().pay(ZYApplication.getInstance().getCurrentActivity(), back.alipayBody, new AlipayUtils.PayCallback() {
            @Override
            public void payInfoResult(Map<String, String> result) {
                String son = new Gson().toJson(result);
                String  resultStatus = result.get("resultStatus");
                switch (resultStatus){
                    case "9000":
                        mView.onPaySuccess();
                        break;
                    case "6001":
                        ZYToast.show((Activity)mView,"支付取消");
                        break;
                   default:
                       mView.onPayFaild();
                       break;

                }
                ZYLog.d(son);
            }

            @Override
            public void payFailde() {
                mView.onPayFaild();
            }
        });
    }

    private void goWeChatPay(final ZYWeChatBack back){
        PayReq payReq = new PayReq();
        payReq.partnerId = back.partnerId;
        payReq.prepayId = back.prepayId;
        payReq.nonceStr = back.nonceStr;
        payReq.timeStamp = back.timeStamp;
        payReq.sign  = back.sign;
        payReq.packageValue = back.packageValue;
        WeChatPayUtils.getInstance().pay(payReq);
        WeChaPayManager.registerPayCallBack("WeChatPay", new WeChatPayCallBack() {
            @Override
            public void payCallBack(int code) {
                switch (code){
                    case 0:
                        mView.onPaySuccess();
                        break;
                    case -1:
                        mView.onPayFaild();
                        break;
                    case -2:
                        ZYToast.show((Activity)mView,"支付取消");
                        break;
                }
            }
        });
    }
}
