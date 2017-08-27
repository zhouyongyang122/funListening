package com.funlisten.business.profile.model.bean;

import com.funlisten.base.bean.ZYIBaseBean;
import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.business.photo.ZYPhoto;

import java.util.List;

/**
 * Created by gd on 2017/7/17.
 */

public class ZYProfileHeaderInfo implements ZYIBaseBean {
    public ZYUser user;
    public ZYListResponse<ZYPhoto> response;
    public int totalCount;

    public ZYProfileHeaderInfo(ZYUser user, ZYListResponse<ZYPhoto> response, int totalCount) {
        this.user = user;
        this.response = response;
        this.totalCount = totalCount;
    }
}
