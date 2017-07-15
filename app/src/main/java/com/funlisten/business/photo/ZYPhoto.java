package com.funlisten.business.photo;

import com.funlisten.base.bean.ZYIBaseBean;
import com.funlisten.base.view.ZYPicker;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYPhoto implements ZYIBaseBean {

    public int id;

    public String photoUrl;

    ZYPicker zyPicker;

    public boolean isEdit = false;
    public boolean isSelect = false;
}
