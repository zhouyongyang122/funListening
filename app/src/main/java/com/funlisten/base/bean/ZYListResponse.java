package com.funlisten.base.bean;

import java.util.List;

/**
 * Created by ZY on 17/6/13.
 */

public class ZYListResponse<D> implements ZYIBaseBean {

    public List<D> data;

    public boolean hasNext;//是否有下一页

    public boolean isEmpty;//是否为空

    public int pageIndex;//当前页

    public int pageSize;//每页大小

    public int totalCount;//总条数

    public int totalPage;//总页数
}
