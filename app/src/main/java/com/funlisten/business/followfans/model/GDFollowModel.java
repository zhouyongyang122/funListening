package com.funlisten.business.followfans.model;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.user.model.ZYUserList;


import rx.Observable;

/**
 * Created by gd on 2017/7/12.
 */

public class GDFollowModel extends ZYBaseModel {
    public Observable<ZYResponse<ZYListResponse<ZYUserList>>> follows(String userId, int pageIndex, int pageSize){
        return mApi.follows(userId,pageIndex,pageSize);
    }
   
}
