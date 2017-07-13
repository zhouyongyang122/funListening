package com.funlisten.base.bean;

/**
 * Created by ZY on 17/3/16.
 */

public class ZYResponse<D> implements ZYIBaseBean {

    /*
    * 响应成功
    */
    public static final String CODE_STATUS_SUCCESS = "ok";
    /**
     * 参数错误
     */
    public static final String CODE_PARAM_ERROR = "501";
    /**
     * 业务异常
     */
    public static final String BIZ_SERVICE_ERROR = "502";
    /**
     * 服务器异常
     */
    public static final String CODE_SERVICE_ERROR = "503";
    /**
     * token失效
     */
    public static final String CODE_TOKEN_EXPIRE = "601";

    public String status = "";

    public String msg;

    public D data;

}
