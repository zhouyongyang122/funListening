package com.funlisten.service.downNet;

/**
 * Created by ZY on 17/3/17.
 */

public class ZYResponseEntity<D> {

    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAIL = 0;

    public int status;

    public String msg;

    public D data;
}
