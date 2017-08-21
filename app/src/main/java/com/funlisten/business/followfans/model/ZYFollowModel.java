package com.funlisten.business.followfans.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.set.model.bean.ZYUserList;


import rx.Observable;

/**
 * Created by gd on 2017/7/12.
 */

public class ZYFollowModel extends ZYBaseModel {

    public static final int FOLLOW_TYPE = 0;

    public static final int FANS_TYPE = 1;

    public Observable<ZYResponse<ZYListResponse<ZYUserList>>> follows(String userId, int pageIndex, int pageSize) {
        return mApi.follows(userId, pageIndex, pageSize);
    }

    public Observable<ZYResponse<ZYListResponse<ZYUserList>>> fans(String userId, int pageIndex, int pageSize) {
        return mApi.fans(userId, pageIndex, pageSize);
    }

}
