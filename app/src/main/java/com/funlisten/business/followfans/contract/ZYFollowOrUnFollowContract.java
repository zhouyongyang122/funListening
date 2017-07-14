package com.funlisten.business.followfans.contract;

import com.funlisten.business.user.model.ZYUserList;

/**
 * Created by Administrator on 2017/7/13.
 */

public interface ZYFollowOrUnFollowContract{
    void onFollow(ZYUserList data);
    void onUnFollow(ZYUserList data);
}
