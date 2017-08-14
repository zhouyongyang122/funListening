package com.funlisten.thirdParty.image;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

/**
 * 图片加载接口
 */
public interface ZYIImageLoader {

    void initDefaultRes(int head, int errorHead, int pic, int errorPic, int color);

    ///////////////////////////////////////////////////////////////////////////
    // 加载普通网络图片
    ///////////////////////////////////////////////////////////////////////////
    void loadImage(Object object, ImageView imageView, String imgUrl);

    void loadImage(Object object, ImageView imageView, String imgUrl, OnLoadImageFinishListener onLoadImageFinishListener);

    void loadImage(Object object, ImageView imageView, String imgUrl, int resDefault, int resError);

    void loadImage(Object object, ImageView imageView, String imgUrl, int resDefault, int resError, OnLoadImageFinishListener onLoadImageFinishListener);

    ///////////////////////////////////////////////////////////////////////////
    // 加载圆形网络图片
    ///////////////////////////////////////////////////////////////////////////
    void loadCircleImage(Object object, ImageView imageView, String imgUrl);

    void loadCircleImage(Object object, ImageView imageView, String imgUrl, OnLoadImageFinishListener onLoadImageFinishListener);

    void loadCircleImage(Object object, ImageView imageView, String imgUrl, int resDefault, int resError);

    void loadCircleImage(Object object, ImageView imageView, String imgUrl, int resDefault, int resError, OnLoadImageFinishListener onLoadImageFinishListener);

    ///////////////////////////////////////////////////////////////////////////
    // 加载毛玻璃网络图片
    ///////////////////////////////////////////////////////////////////////////
    void loadBlurImage(Object object, ImageView imageView, String imgUrl);

    void loadBlurImage(Object object, ImageView imageView, String imgUrl, int resDefault, int resError);

    ///////////////////////////////////////////////////////////////////////////
    // 加载圆角网络图片
    ///////////////////////////////////////////////////////////////////////////
    void loadRoundImage(Object object, ImageView imageView, String imgUrl, int radius);

    void loadRoundImage(Object object, ImageView imageView, String imgUrl, int radius, OnLoadImageFinishListener onLoadImageFinishListener);

    void loadRoundImage(Object object, ImageView imageView, String imgUrl, int radius, int resDefault, int resError);

    void loadRoundImage(Object object, ImageView imageView, String imgUrl, int radius, int resDefault, int resError, OnLoadImageFinishListener onLoadImageFinishListener);

    void loadFromMediaStore(Object object, final String imgUrl,final OnLoadLocalImageFinishListener onLoadLocalImageFinishListener);

    boolean checkParams(Object object, ImageView imageView, String imgUrl);

    void stopLoad(ImageView imageView);

    ZYIImageLoader setFitType(int fitType);

    ZYIImageLoader setHeight(int height);

    ZYIImageLoader setWidth(int width);

    ZYIImageLoader setColorFilter(int color);

    interface OnLoadLocalImageFinishListener {

        void onLoadFinish(@Nullable Bitmap bitmap);
    }

    interface OnLoadImageFinishListener {

        void onLoadFinish(@Nullable Drawable drawable);
    }
}
