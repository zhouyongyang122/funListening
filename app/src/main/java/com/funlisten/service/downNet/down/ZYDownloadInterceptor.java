package com.funlisten.service.downNet.down;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by ZY on 17/3/17.
 */

public class ZYDownloadInterceptor implements Interceptor {

    private ZYDownloadProgressListener listener;

    public ZYDownloadInterceptor(ZYDownloadProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        return originalResponse.newBuilder()
                .body(new ZYDownloadResponseBody(originalResponse.body(), listener))
                .build();
    }
}
