package com.funlisten.business.user.model;

import com.funlisten.base.bean.ZYIBaseBean;
import com.funlisten.business.login.model.bean.ZYUser;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYUserList implements ZYIBaseBean {

    public ZYUser user;

    public String followStatus;//关注状态（no_follow：未关注，following：关注中，be_follow：被关注，mutually_followed：互相关注）

    public int fromUserId;//关注者用户id

    public int id;

    public int toUserId;//被关注者id
}
