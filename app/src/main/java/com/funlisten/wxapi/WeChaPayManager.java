package com.funlisten.wxapi;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by gongdong on 2017/5/10.
 */

public class WeChaPayManager {
    private final  static HashMap<String,WeChatPayCallBack> callBackMap = new HashMap<>();

    public static void registerPayCallBack(String keyTag,WeChatPayCallBack callBack){
        if(TextUtils.isEmpty(keyTag) || callBack == null)return;
        callBackMap.put(keyTag,callBack);
    }

    public static void notifyCallBack(int code){
        Iterator iter = callBackMap.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry<String,WeChatPayCallBack> entry = (Map.Entry) iter.next();
            WeChatPayCallBack callBack = entry.getValue();
            if(callBack !=null)callBack.payCallBack(code);
        }
        cleanAll();
    }

    private static void cleanTagCallBack(String keyTag){
        if(TextUtils.isEmpty(keyTag))return;
        callBackMap.remove(keyTag);
    }

    private static void cleanAll(){
        callBackMap.clear();
    }
}
