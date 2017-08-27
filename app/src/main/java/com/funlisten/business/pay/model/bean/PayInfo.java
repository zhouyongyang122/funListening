package com.funlisten.business.pay.model.bean;

import com.funlisten.base.bean.ZYIBaseBean;

/**
 * Created by Administrator on 2017/7/29.
 */

public class PayInfo implements ZYIBaseBean {
    public String productId;
    public String productType;

    public PayInfo() {
    }

    public PayInfo(String productId, String productType) {
        this.productId = productId;
        this.productType = productType;
    }
}
