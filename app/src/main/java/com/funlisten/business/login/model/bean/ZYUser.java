package com.funlisten.business.login.model.bean;

import com.funlisten.service.db.ZYDBManager;
import com.funlisten.service.db.entity.ZYBaseEntity;
import com.funlisten.service.db.entity.ZYUserDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by ZY on 17/4/2.
 */

@Entity
public class ZYUser extends ZYBaseEntity {
    public String id;
    @Id
    public String userId;

    public String phone;

    public String nickname;

    public String openWechatId;

    public String avatarUrl;

    public String avatar;

    public int albumCount;

    public String intro;

    public String sex;

    public int fans;

    public int follow;

    public String token;

    public boolean isLoginUser;

    @Generated(hash = 193195294)
    public ZYUser() {
    }

    @Generated(hash = 1172012738)
    public ZYUser(String id, String userId, String phone, String nickname,
            String openWechatId, String avatarUrl, String avatar, int albumCount,
            String intro, String sex, int fans, int follow, String token,
            boolean isLoginUser) {
        this.id = id;
        this.userId = userId;
        this.phone = phone;
        this.nickname = nickname;
        this.openWechatId = openWechatId;
        this.avatarUrl = avatarUrl;
        this.avatar = avatar;
        this.albumCount = albumCount;
        this.intro = intro;
        this.sex = sex;
        this.fans = fans;
        this.follow = follow;
        this.token = token;
        this.isLoginUser = isLoginUser;
    }

    @Override
    public long save() {
        ZYUserDao userDao = ZYDBManager.getInstance().getWritableDaoSession().getZYUserDao();
        return userDao.insertOrReplace(this);
    }

    @Override
    public long update() {
        ZYUserDao userDao = ZYDBManager.getInstance().getWritableDaoSession().getZYUserDao();
        return userDao.insertOrReplace(this);
    }

    @Override
    public void delete() {
        ZYUserDao userDao = ZYDBManager.getInstance().getWritableDaoSession().getZYUserDao();
        userDao.delete(this);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenWechatId() {
        return this.openWechatId;
    }

    public void setOpenWechatId(String openWechatId) {
        this.openWechatId = openWechatId;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getAlbumCount() {
        return this.albumCount;
    }

    public void setAlbumCount(int albumCount) {
        this.albumCount = albumCount;
    }

    public String getIntro() {
        return this.intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getFans() {
        return this.fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getFollow() {
        return this.follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getIsLoginUser() {
        return this.isLoginUser;
    }

    public void setIsLoginUser(boolean isLoginUser) {
        this.isLoginUser = isLoginUser;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
