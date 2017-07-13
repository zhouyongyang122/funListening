package com.third.loginshare.net;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * 请求工具接口
 *
 * @author panbinxian
 * @Data 2014-8-4
 */
public interface IRequestTool {
    public String httpPostRequestJson(String servAddr, List<NameValuePair> params) throws Exception;

    public String httpGetRequestJson(String servAddr) throws Exception;

    public String httpsGetRequest(String servHttpsAddr) throws Exception;

    public String httpsPostRequest(String servHttpsAddr, String xmlBody) throws Exception;

    public void setTimeout(int timeout);
}
