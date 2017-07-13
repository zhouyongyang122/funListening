package com.funlisten.service.db.entity;

import com.funlisten.base.bean.ZYIBaseBean;

/**
 * Created by ZY on 17/3/17.
 */

public abstract class ZYBaseEntity implements ZYIBaseBean {

    public abstract long save();

    public abstract long update();

    public abstract void delete();
}
