package com.funlisten.business.mylike.model.bean;

import com.funlisten.base.bean.ZYIBaseBean;

/**
 * Created by Administrator on 2017/7/25.
 */

public class ZYMyLikeHeadInfo implements ZYIBaseBean {
    public int totalCount;

    public ZYMyLikeHeadInfo(int totalCount) {
        this.totalCount = totalCount;
    }
}
