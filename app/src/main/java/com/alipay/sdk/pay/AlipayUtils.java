package com.alipay.sdk.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * Created by hp on 2016/12/27.
 */

public class AlipayUtils {

    private static final int PAY_FAILDE = 400;
    private PayCallback callback;
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PAY_FAILDE:
                    if(callback!=null) callback.payFailde();
                    break;
                default:
                    if(callback!=null) callback.payInfoResult((Map<String, String>) msg.obj);
                    break;
            }

        }
    };

    private static AlipayUtils inStance;

    public static AlipayUtils getInStance(){
        if(inStance == null){
            inStance = new AlipayUtils();
        }
        return inStance;
    }

    private AlipayUtils(){}

    public void pay(final Activity context,final  String orderInfo,PayCallback callback){
        this.callback = callback;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message message = handler.obtainMessage();
                message.obj = result;
                handler.sendMessage(message);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /***
     * 获取签名的订单号
     * **/
    public void  getSign(final Activity context, String orderNo, final PayCallback callback){
        this.callback = callback;
//        String url = DemoServers.chatRoomAPIServer() + Contants.URL_ALIPAY_SIGN;
//        RequestParams params = new RequestParams(url);
//        params.addParameter("orderNo",orderNo);
//        ChatRoomHttpClient.getInstance().requestGet(params, context, new ChatRoomHttpClient.ChatRoomHttpCallback<String>() {
//            @Override
//            public void onSuccess(String res) {
//                if (StringUtil.isEmpty(res)) {
//                    handler.sendEmptyMessage(PAY_FAILDE);
//                    return;
//                }
//                JSONObject response = JsonUtils.parseObjects(res);
//                String code = response.getString("code");
//                JSONObject object = response.getJSONObject("object");
//                String orderInfo = object.getString("orderInfo");
//                if (!Contants.SUCCESS_CODE.equals(code) || TextUtils.isEmpty(orderInfo)) {
//                    handler.sendEmptyMessage(PAY_FAILDE);
//                    return;
//                }
//                pay(context,orderInfo,callback);
//                DialogMaker.dismissProgressDialog();
//            }
//
//            @Override
//            public void onFailed(int code, String errorMsg) {
//                handler.sendEmptyMessage(PAY_FAILDE);
//            }
//        });
    }

    public interface PayCallback{
        public void payInfoResult(Map<String, String> result);
        public void payFailde();
    }
}
