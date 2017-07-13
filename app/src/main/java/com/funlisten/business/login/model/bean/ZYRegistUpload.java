package com.funlisten.business.login.model.bean;

import com.funlisten.base.bean.ZYIBaseBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZY on 17/7/1.
 */

public class ZYRegistUpload implements ZYIBaseBean {

    public String phone;

    public String password;

    public String code;

    public String nickname;

    //性别 male/female
    public String sex;

    //年龄范围 00 70 80
    public String ageRange;

    public Map<String, String> getParams() {
        Map<String, String> paramas = new HashMap<String, String>();
        paramas.put("phone", phone);
        paramas.put("password", password);
        paramas.put("code", code);
        paramas.put("nickname", nickname);
        paramas.put("ageRange", ageRange);
        paramas.put("sex", sex);
        return paramas;
    }
}
