package com.funlisten.service.net;

import android.text.TextUtils;

import com.funlisten.utils.ZYLog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ZY on 17/4/13.
 */

public class ZYOkHttpNetManager {

    private static ZYOkHttpNetManager instance;

    private OkHttpClient mOkHttpClient;

    private boolean isCancle;

    private ZYOkHttpNetManager() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .build();
    }

    public static ZYOkHttpNetManager getInstance() {
        if (instance == null) {
            instance = new ZYOkHttpNetManager();
        }
        return instance;
    }

    public void reset() {
        isCancle = false;
    }

    public void requestGet(final Class classBean, String url, final OkHttpNetListener listener) {
        if (TextUtils.isEmpty(url) || listener == null) {
            return;
        }
        try {
            final Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ZYLog.e(ZYOkHttpNetManager.class.getSimpleName(), "onFailure: " + e.getMessage());
                    if (listener != null && !isCancle) {
                        listener.onFailure("网络异常,请重新尝试");
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (listener != null && !isCancle) {
                        try {
                            listener.onSuccess(new Gson().fromJson(response.body().toString(), classBean));
                        } catch (Exception e) {
                            ZYLog.e(ZYOkHttpNetManager.class.getSimpleName(), "onResponse-error: " + e.getMessage());
                            listener.onFailure("网络异常,请重新尝试");
                        }
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    public void requestPost(final Class classBean, String url, Map<String, String> params, final OkHttpNetListener listener) {
        if (TextUtils.isEmpty(url) || listener == null) {
            return;
        }
        try {

            FormBody.Builder bodyBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    bodyBuilder.add(entry.getKey(), entry.getValue());
                }
            }

            Request request = new Request.Builder()
                    .url(url)
                    .post(bodyBuilder.build())
                    .build();

            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ZYLog.e(ZYOkHttpNetManager.class.getSimpleName(), "onFailure: " + e.getMessage());
                    if (listener != null && !isCancle) {
                        listener.onFailure("网络异常,请重新尝试");
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (listener != null && !isCancle) {
                        try {
                            String result = response.body().string();
                            listener.onSuccess(new Gson().fromJson(result, classBean));
                        } catch (Exception e) {
                            ZYLog.e(ZYOkHttpNetManager.class.getSimpleName(), "onResponse-error: " + e.getMessage());
                            listener.onFailure("网络异常,请重新尝试");
                        }
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    public void cancle() {
        isCancle = true;
        mOkHttpClient.dispatcher().cancelAll();
    }

    public interface OkHttpNetListener<T> {
        void onFailure(String message);

        void onSuccess(T response);
    }

}
