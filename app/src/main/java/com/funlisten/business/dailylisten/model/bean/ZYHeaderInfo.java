package com.funlisten.business.dailylisten.model.bean;

import com.funlisten.base.bean.ZYIBaseBean;

/**
 * Created by Administrator on 2017/7/20.
 */

public class ZYHeaderInfo implements ZYIBaseBean {
    public int totalCount;

    public ZYHeaderInfo(int totalCount) {
        this.totalCount = totalCount;
    }
}
