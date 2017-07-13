package com.funlisten.base.html5;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/4/9.
 */

public class ZYHtml5UrlRequest {
    private static ZYHtml5UrlRequest instance;

    private ZYHtml5UrlBean paramas;

    private Html5UrlRequestListener listener;

    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    private ZYHtml5UrlRequest() {

    }

    public static ZYHtml5UrlRequest getInstance() {
        if (instance == null) {
            instance = new ZYHtml5UrlRequest();
        }
        return instance;
    }

    public void getParamas(final Html5UrlRequestListener requestListener) {
        this.listener = requestListener;
        if (paramas != null) {
            if (listener != null) {
                listener.onHtm5UrlResponsed(paramas, null);
            }
        } else {
            if (listener != null) {
                listener.onHtm5UrlRequestStart();
            }


//            mSubscriptions.add(ZYNetSubscription.subscription(new ZYBaseModel().getHtml5Urls(), new ZYNetSubscriber<ZYResponse<ZYHtml5UrlBean>>() {
//                @Override
//                public void onSuccess(ZYResponse<ZYHtml5UrlBean> response) {
//                    paramas = response.data;
//                    if (listener != null) {
//                        listener.onHtm5UrlResponsed(paramas, null);
//                    }
//                }
//
//                @Override
//                public void onFail(String message) {
//                    if (listener != null) {
//                        listener.onHtm5UrlResponsed(null, message);
//                    }
//                }
//            }));
        }
    }

    public void setListener(Html5UrlRequestListener listener) {
        this.listener = listener;
    }

    public void clear() {
        try {
            this.listener = null;
            mSubscriptions.unsubscribe();
        } catch (Exception e) {

        }
    }

    public interface Html5UrlRequestListener {
        void onHtm5UrlRequestStart();

        void onHtm5UrlResponsed(ZYHtml5UrlBean urlBean, String errorMsg);
    }
}
