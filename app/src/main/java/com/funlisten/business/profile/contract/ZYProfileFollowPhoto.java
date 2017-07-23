package com.funlisten.business.profile.contract;

/**
 * Created by gd on 2017/7/17.
 */

public interface ZYProfileFollowPhoto {
    void onFollow(String userId);
    void intoPhoto(String userId);
}
