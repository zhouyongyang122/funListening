package com.funlisten.base.mvp;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.html5.ZYHtml5UrlBean;
import com.funlisten.service.net.ZYNetManager;
import com.funlisten.service.net.ZYRequestApi;

import rx.Observable;

/**
 * Created by ZY on 17/3/14.
 */

public class ZYBaseModel {

    protected ZYRequestApi mApi;

    public ZYBaseModel() {
        mApi = ZYNetManager.shareInstance().getApi();
    }

//    public Observable<ZYResponse<ZYHtml5UrlBean>> getHtml5Urls() {
//        return mApi.getHtml5Urls();
//    }
}
