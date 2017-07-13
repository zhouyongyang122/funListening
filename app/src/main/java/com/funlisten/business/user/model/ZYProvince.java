package com.funlisten.business.user.model;

import com.funlisten.base.bean.ZYIBaseBean;

import java.util.List;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYProvince implements ZYIBaseBean {

    public List<City> cities;

    public int id;

    public String provinceCode;

    public String provinceName;

    public class City {
        public String cityCode;
        public String cityName;
        public int id;
        public String provinceCode;
    }
}
