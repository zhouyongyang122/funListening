package com.funlisten.business.order;

import com.funlisten.base.bean.ZYIBaseBean;

import java.util.List;

/**
 * Created by gd on 2017/7/29.
 */

public class ZYWeChatBack implements ZYIBaseBean {

    public int userId;

    public String orderNum;//订单号

    public String orderStatus;
    public String payType;
    public String payment;
    public String prepayId;
    public String productDetail;
    public String sign;
    public String partnerId;
    public String timeStamp;
    public String nonceStr;
    public String packageValue;

    public List<Product> productList;

    public class Product implements ZYIBaseBean{
        public String productId;
        public String productName;
        public String productType;
    }
}
