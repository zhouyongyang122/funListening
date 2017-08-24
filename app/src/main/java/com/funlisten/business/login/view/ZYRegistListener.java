package com.funlisten.business.login.view;

/**
 * Created by ZY on 17/7/1.
 */

public interface ZYRegistListener {
    void checkPhoneIsExists();
    void completeMobile();
    void completeCode();
    void completeName();
    void completeGender();
    void sendCode();
}
