package com.funlisten.business.album.model.bean;

import com.funlisten.base.bean.ZYIBaseBean;
import com.funlisten.business.login.model.bean.ZYUser;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYComment implements ZYIBaseBean {

    public ZYComment(int id){
        this.id = id;
    }

    public String content;

    public int id;

    public int objectId;//评论对象id

    public int objectUserId;//评论对象用户id（专辑上传者id）

    public String type;//评论对象类型；album：专辑，audio：单集

    public String userId;//用户id

    public String gmtCreate;

    public boolean isLiked;//是否点赞

    public int likeCount;//点赞数

    public ZYUser user;
}
