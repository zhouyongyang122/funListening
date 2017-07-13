package com.funlisten.service.net;


import com.funlisten.ZYApplication;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYToast;

/**
 * Created by ZY on 17/3/16.
 */

public class ZYNetSubscriber<R extends ZYResponse> extends rx.Subscriber<R> {

    public static String errorMsg = "网络异常,请重新尝试";

    @Override
    /**
     * 事件开始前的处理
     */
    public void onStart() {
        super.onStart();
        //可以处理token失效的问题
    }

    @Override
    /**
     * 事件完成 子类不要继承
     */
    public void onCompleted() {
    }

    @Override
    /**
     * 事件出错 子类不要继承
     */
    public void onError(final Throwable e) {
        if (e != null) {
            ZYLog.e(e.getMessage());
        }
        onFail(errorMsg);
    }

    @Override
    /**
     * 事件响应 子类不要继承
     */
    public void onNext(R response) {
        if (response != null) {

            if (response.status.equals(ZYResponse.CODE_STATUS_SUCCESS)) {
                onSuccess(response);
            } else if (response.status.equals(ZYResponse.CODE_TOKEN_EXPIRE)) {
                //token失效
                try {
//                        ZYToast.show(SRApplication.getInstance(), "登录信息失效,请重新登录");
//                        ZYApplication.getInstance().getCurrentActivity().startActivity(SRLoginActivity.createIntent(SRApplication.getInstance().getCurrentActivity()));
                } catch (Exception e) {
                    ZYLog.e(getClass().getSimpleName(), "onNext:" + e.getMessage());
                }
                onFail("登录信息失效,请重新登录");
            } else {
                onFail(response.msg);
            }
        } else {
            onFail(errorMsg);
        }
    }

    /**
     * 请求成功 子类继承
     *
     * @param response
     */
    public void onSuccess(R response) {

    }

    /**
     * 请求失败,子类继承
     *
     * @param message
     */
    public void onFail(String message) {
        if (message != null) {
            ZYToast.show(ZYApplication.getInstance(), message);
        } else {
            ZYToast.show(ZYApplication.getInstance(), errorMsg);
        }
    }

    /**
     * 其它响应处理(服务器返回的status不为0且不为1的响应),子类继承
     *
     * @param status
     */
    public void onOtherResponse(int status, String message) {
        onFail(message);
    }
}
