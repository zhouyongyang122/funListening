package com.funlisten.service.net;

import android.text.TextUtils;

import com.funlisten.ZYPreferenceHelper;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYStringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ZY on 17/3/16.
 */

public class ZYNetManager {

    private static ZYNetManager instance;

    private ZYRequestApi sRxjavaApi;

    private ZYNetConfig mNetConfig;

    private ZYNetManager() {
        mNetConfig = new ZYNetConfig();
    }

    public static ZYNetManager shareInstance() {
        if (instance == null) {
            instance = new ZYNetManager();
        }
        return instance;
    }

    public OkHttpClient.Builder getOkHttpBuilder() {
        return new OkHttpClient.Builder()
                .addInterceptor(new FZHeaderInterceptor(mNetConfig.getHeaders()))
                .addInterceptor(new HttpLoggingInterceptor(new ZYLog())
                        .setLevel(HttpLoggingInterceptor.Level.BODY));

//        .addInterceptor(new FZParamsInterceptor())
    }

    /**
     * 获取请求api
     *
     * @return
     */
    public ZYRequestApi getApi() {
        if (sRxjavaApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(getOkHttpBuilder().build())
                    .baseUrl(ZYNetConfig.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            sRxjavaApi = retrofit.create(ZYRequestApi.class);
        }
        return sRxjavaApi;
    }

    /**
     * 请求头拦截器 用来封装公共的头信息
     */
    public class FZHeaderInterceptor implements Interceptor {

        private Map<String, String> mHeaders;

        public FZHeaderInterceptor(Map<String, String> headers) {
            mHeaders = headers;
        }

        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            if (mHeaders != null) {
                for (Map.Entry<String, String> entry : mHeaders.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
                if (ZYUserManager.getInstance().getUser().token != null) {
                    builder.addHeader("token", ZYUserManager.getInstance().getUser().token);
                }
            }
            Request request = builder.build();
            return chain.proceed(request);
        }
    }

//    /**
//     * 参数拦截器，添加公共参数
//     */
//    public class FZParamsInterceptor implements Interceptor {
//
//        public FZParamsInterceptor() {
//        }
//
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            HashMap<String, String> baseParams = new HashMap<>();
//            baseParams = mNetConfig.getDefParams();
//
//            Request request = chain.request();
//            if (isAddCommonParams(request.url().uri().getPath())) {
//                Request.Builder builder = chain.request().newBuilder();
//                if (request.method().equalsIgnoreCase("post")) {
//                    RequestBody body = chain.request().body();
//                    if (body != null) {
//                        Buffer buffer = new Buffer();
//                        body.writeTo(buffer);
//                        HashMap<String, String> params = new HashMap<>(baseParams);
//                        //合并参数
//
//                        boolean isForm = false;
//                        try {
//                            //如果post参数只有uid和auth_token 会报异常
//                            String jsonString = buffer.readString(Charset.forName("UTF-8"));
//                            JSONObject jsonObject = new JSONObject(jsonString);
//                            Iterator<String> keys = jsonObject.keys();
//                            while (keys.hasNext()) {
//                                String key = keys.next();
//                                String value = jsonObject.optString(key);
//                                if (!TextUtils.isEmpty(value)) {
//                                    params.put(key, value);
//                                }
//                            }
//                        } catch (JSONException e) {
//                            if (body instanceof FormBody) {
//                                isForm = true;
//                                FormBody formBody = (FormBody) body;
//                                int size = formBody.size();
//                                for (int i = 0; i < size; i++) {
//                                    String key = formBody.name(i);
//                                    String value = formBody.value(i);
//                                    if (!TextUtils.isEmpty(value)) {
//                                        params.put(key, value);
//                                    }
//                                }
//                            }
//                        }
//
//                        if (isForm) {
//                            FormBody.Builder formBodyBuilder = new FormBody.Builder();
//                            for (Map.Entry<String, String> entry : params.entrySet()) {
//                                if (entry.getValue() != null) {
//                                    formBodyBuilder.add(entry.getKey(), entry.getValue());
//                                }
//                            }
//                            builder.post(formBodyBuilder.build());
//                        } else {
//                            //转成json字符串出入请求体
//                            Gson gson = new GsonBuilder().create();
//                            String json = gson.toJson(params);
//                            body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
//                            builder.post(body);
//                        }
//
//                    }
//                } else if (request.method().equalsIgnoreCase("get")) {
//                    HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
//                    for (Map.Entry<String, String> entry : baseParams.entrySet()) {
//                        httpUrlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
//                    }
//
//                    HttpUrl url = httpUrlBuilder.build();
//                    Map<String, String> map = new HashMap<>();
//                    for (String name : url.queryParameterNames()) {
//                        map.put(name, url.queryParameter(name));
//                    }
//
//                    for (Map.Entry<String, String> entry : map.entrySet()) {
//                        httpUrlBuilder.setQueryParameter(entry.getKey(), entry.getValue().trim());
//                    }
//
//                    builder.url(httpUrlBuilder.build());
//                }
//                return chain.proceed(builder.build());
//            } else {
//                return chain.proceed(chain.request());
//            }
//        }
//    }

//    /**
//     * 通过url判断是否需要添加公共参数
//     */
//    private boolean isAddCommonParams(String url) {
//        return !(url == null || url.isEmpty());
//    }
}
