package com.funlisten.business.pay.model.bean;

/**
 * Created by Administrator on 2017/7/29.
 */

public class PayInfo {
    public String productId;
    public String productType;

    public PayInfo() {
    }

    public PayInfo(String productId, String productType) {
        this.productId = productId;
        this.productType = productType;
    }
}
