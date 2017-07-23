package com.funlisten.business.login.model;

import android.text.TextUtils;

import com.funlisten.ZYApplication;
import com.funlisten.service.db.ZYDBManager;
import com.funlisten.service.db.entity.ZYUserDao;
import com.funlisten.business.login.activity.ZYLoginActivity;
import com.funlisten.business.login.model.bean.ZYUser;

import java.util.List;

/**
 * Created by ZY on 17/4/2.
 */

public class ZYUserManager {

    private static ZYUserManager instance;

    private ZYUser user;

    private ZYUserManager() {
        user = getLoginUser();
    }

    public static ZYUserManager getInstance() {
        if (instance == null) {
            instance = new ZYUserManager();
        }
        return instance;
    }

    private ZYUser getLoginUser() {
        ZYUserDao userDao = ZYDBManager.getInstance().getReadableDaoSession().getZYUserDao();
        List<ZYUser> users = userDao.loadAll();
        if (users == null || users.size() < 0) {
            return getGuesterUser();
        } else {
            for (ZYUser user : users) {
                if (user.isLoginUser) {
                    return user;
                }
            }
        }
        return new ZYUser();
    }

    public ZYUser getUser() {
        return user;
    }

    public ZYUser getGuesterUser() {
        ZYUser guesterUser = new ZYUser();
        guesterUser.id = "-1";
        return guesterUser;
    }

    public void loginOut() {
        user.isLoginUser = false;
        user.update();
        user = getGuesterUser();
    }

    public void setUser(ZYUser user) {
        if (user != null) {
            this.user = user;
            this.user.isLoginUser = true;
            this.user.update();
        }
    }

    public void updateUser(ZYUser user) {
        if (isGuesterUser(false)) {
            setUser(user);
        } else {
            this.user.nickname = user.nickname;
            this.user.avatarUrl = user.avatarUrl;
            this.user.albumCount = user.albumCount;
            this.user.fans = user.fans;
            this.user.follow = user.follow;
            this.user.intro = user.intro;
            this.user.sex = user.sex;
        }
    }


    public boolean isGuesterUser(boolean needIntentToLogin) {
        boolean isGuester = TextUtils.isEmpty(user.id);
        if (needIntentToLogin && isGuester) {
            //跳到登录
            ZYApplication.getInstance().getCurrentActivity().startActivity(ZYLoginActivity.createIntent(ZYApplication.getInstance().getCurrentActivity()));
        }
        return isGuester;
    }
}
