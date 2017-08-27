package com.funlisten.business.comment.model.bean;

import com.funlisten.base.bean.ZYIBaseBean;

/**
 * Created by Administrator on 2017/7/26.
 */

public class ZYCommentHeaderInfo implements ZYIBaseBean{
    public int totalCount;

    public ZYCommentHeaderInfo(int totalCount) {
        this.totalCount = totalCount;
    }
}
