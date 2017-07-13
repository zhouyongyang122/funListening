package com.funlisten.business.order;

import com.funlisten.base.bean.ZYIBaseBean;

import java.util.List;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYAlipayBack implements ZYIBaseBean {

    public int userId;

    public String orderNum;//订单号

    public String alipayBody;//支付宝sdk body信息

    public List<Product> productList;

    public class Product implements ZYIBaseBean{
        public String productId;
        public String productName;
        public String productType;
    }
}
