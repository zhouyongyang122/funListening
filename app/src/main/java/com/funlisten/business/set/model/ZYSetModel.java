package com.funlisten.business.set.model;


import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.set.model.bean.ZYMsg;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ZY on 17/4/9.
 */

public class ZYSetModel extends ZYBaseModel {

    public Observable<ZYResponse<ZYListResponse<ZYMsg>>> getSysMsgs(int pageIndex, int rows) {
        return mApi.getFeedBacks(pageIndex, rows);
    }
}
