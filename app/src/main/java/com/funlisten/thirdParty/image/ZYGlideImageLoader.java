package com.funlisten.thirdParty.image;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.funlisten.ZYApplication;
import com.funlisten.thirdParty.image.imageTransform.ZYBlurTransformation;
import com.funlisten.thirdParty.image.imageTransform.ZYCropCircleTransformation;
import com.funlisten.thirdParty.image.imageTransform.ZYRoundedCornersTransformation;

public class ZYGlideImageLoader implements ZYIImageLoader {

    private int mDefaultHead;
    private int mErrorHead;
    private int mDefaultPic;
    private int mErrorPic;
    private int mDefaultColor;

    private int mFitType;
    private int mHeight;
    private int mWidth;
    private int mColorFilter;

    public ZYIImageLoader setHeight(int height) {
        this.mHeight = height;
        return this;
    }

    public ZYIImageLoader setWidth(int width) {
        this.mWidth = width;
        return this;
    }

    @Override
    public ZYIImageLoader setColorFilter(int color) {
        this.mColorFilter = color;
        return this;
    }

    public ZYIImageLoader setFitType(int fitType) {
        this.mFitType = fitType;
        return this;
    }

    @Override
    public void initDefaultRes(int head, int errorHead, int pic, int errorPic, int color) {
        mDefaultHead    = head;
        mErrorHead      = errorHead;
        mDefaultPic     = pic;
        mErrorPic       = errorPic;
        mDefaultColor   = color;
    }

    @Override
    public void loadImage(Object object, ImageView imageView, String imgUrl) {
        loadImage(object, imageView, imgUrl, mDefaultPic, mErrorPic);
    }

    @Override
    public void loadImage(Object object, ImageView imageView, String imgUrl, OnLoadImageFinishListener onLoadImageFinishListener) {
        loadImage(object, imageView, imgUrl, mDefaultPic, mErrorPic, onLoadImageFinishListener);
    }

    @Override
    public void loadImage(Object object, ImageView imageView, String imgUrl, int resDefault, int resError) {
        loadImage(object, imageView, imgUrl, resDefault, resError, null);
    }

    @Override
    public void loadImage(Object object, ImageView imageView, String imgUrl, int resDefault, int resError, final OnLoadImageFinishListener onLoadImageFinishListener) {
        RequestManager requestManager = getRequestManager(object);
        if (checkParams(requestManager,imageView,imgUrl)){
            DrawableRequestBuilder<String> builder;
            builder = requestManager.load(imgUrl);
            if (mFitType == ZYImageLoadHelper.FIT_CENTER){
                builder.fitCenter();
            }else if (mFitType == ZYImageLoadHelper.CENTER_CROP){
                builder.centerCrop();
            }else {
                builder.centerCrop();
            }
            if (mWidth != 0 && mHeight != 0){
                builder.override(mWidth, mHeight);
            }

            if (resDefault != 0) {
                builder.placeholder(resDefault);
            }

            if (resError != 0) {
                builder.error(resError);
            }
            builder.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                            if (onLoadImageFinishListener != null){
                                onLoadImageFinishListener.onLoadFinish(null);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                            if (onLoadImageFinishListener != null){
                                onLoadImageFinishListener.onLoadFinish(glideDrawable);
                            }
                            return false;
                        }
                    })
                    .into(imageView);
        }else if(imageView != null){
            imageView.setImageResource(resError);
        }
    }

    @Override
    public void loadCircleImage(Object object, ImageView imageView, String imgUrl) {
        loadCircleImage(object, imageView, imgUrl, mDefaultHead, mErrorHead);
    }

    @Override
    public void loadCircleImage(Object object, ImageView imageView, String imgUrl, OnLoadImageFinishListener onLoadImageFinishListener) {
        loadCircleImage(object, imageView, imgUrl, mDefaultHead, mErrorHead, onLoadImageFinishListener);
    }

    @Override
    public void loadCircleImage(Object object, ImageView imageView, String imgUrl, int resDefault, int resError) {
        loadCircleImage(object, imageView, imgUrl, resDefault, resError, null);
    }

    @Override
    public void loadCircleImage(Object object, ImageView imageView, String imgUrl, int resDefault, int resError, final OnLoadImageFinishListener onLoadImageFinishListener) {
        RequestManager requestManager = getRequestManager(object);
        if (checkParams(requestManager,imageView,imgUrl)){
            requestManager.load(imgUrl)
                    .centerCrop()
                    .placeholder(resDefault)
                    .error(resError)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new ZYCropCircleTransformation(imageView.getContext()))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                            if (onLoadImageFinishListener != null){
                                onLoadImageFinishListener.onLoadFinish(null);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                            if (onLoadImageFinishListener != null){
                                onLoadImageFinishListener.onLoadFinish(glideDrawable);
                            }
                            return false;
                        }
                    })
                    .into(imageView);
        }else if(imageView != null){
            imageView.setImageResource(resError);
        }
    }

    @Override
    public void loadBlurImage(Object object, ImageView imageView, String imgUrl) {
        loadBlurImage(object, imageView, imgUrl, mDefaultPic, mErrorPic);
    }

    @Override
    public void loadBlurImage(Object object, ImageView imageView, String imgUrl, int resDefault, int resError) {
        RequestManager requestManager = getRequestManager(object);
        if (checkParams(requestManager,imageView,imgUrl)){
            requestManager.load(imgUrl)
                    .centerCrop()
                    .placeholder(resDefault)
                    .error(resError)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new ZYBlurTransformation(imageView.getContext()))
                    .into(imageView);
        }else if(imageView != null){
            imageView.setImageResource(resError);
        }
    }

    @Override
    public void loadRoundImage(Object object, ImageView imageView, String imgUrl, int radius) {
        loadRoundImage(object, imageView, imgUrl, radius, mDefaultColor, mErrorPic);
    }

    @Override
    public void loadRoundImage(Object object, ImageView imageView, String imgUrl, int radius, OnLoadImageFinishListener onLoadImageFinishListener) {
        loadRoundImage(object, imageView, imgUrl, radius, mDefaultColor, mErrorPic, onLoadImageFinishListener);
    }

    @Override
    public void loadRoundImage(Object object, ImageView imageView, String imgUrl, int radius, int resDefault, int resError) {
        loadRoundImage(object, imageView, imgUrl, radius, resDefault, resError, null);
    }

    @Override
    public void loadRoundImage(Object object, ImageView imageView, String imgUrl, int radius, int resDefault, int resError, final OnLoadImageFinishListener onLoadImageFinishListener) {
        RequestManager requestManager = getRequestManager(object);
        if (checkParams(requestManager,imageView,imgUrl)){
            DrawableRequestBuilder<String> builder;
            if (mFitType == ZYImageLoadHelper.FIT_CENTER){
                builder =  requestManager.load(imgUrl).fitCenter();
            }else if (mFitType == ZYImageLoadHelper.CENTER_CROP){
                builder =  requestManager.load(imgUrl).centerCrop();
            }else {
                builder =  requestManager.load(imgUrl).centerCrop();
            }
            if (mWidth != 0 && mHeight != 0){
                builder.override(mWidth, mHeight);
            }
            builder.placeholder(resDefault)
                    .error(resError)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new ZYRoundedCornersTransformation(imageView.getContext(), radius, 0))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            if (onLoadImageFinishListener != null){
                                onLoadImageFinishListener.onLoadFinish(null);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if (onLoadImageFinishListener != null){
                                onLoadImageFinishListener.onLoadFinish(resource);
                            }
                            return false;
                        }
                    })
                    .into(imageView);
        }else if(imageView != null){
            imageView.setImageResource(resError);
        }
    }


    @Override
    public boolean checkParams(Object object, ImageView imageView, String imgUrl) {
        return (imageView != null && !TextUtils.isEmpty(imgUrl) && object != null);
    }

    @Override
    public void stopLoad(ImageView imageView) {
        if (imageView != null) {
            Glide.clear(imageView);
        }
    }

    private RequestManager getRequestManager(Object object){

        RequestManager requestManager;

        if (object instanceof FragmentActivity){
            requestManager = Glide.with((Activity)object);
        }else if (object instanceof Fragment){
            requestManager = Glide.with((Fragment)object);
        }else if (object instanceof android.app.Fragment){
            requestManager = Glide.with((android.app.Fragment)object);
        }else if (object instanceof Activity){
            requestManager = Glide.with((Activity)object);
        }else if (object instanceof Context){
            requestManager = Glide.with((Context)object);
        } else {
            requestManager = Glide.with(ZYApplication.getInstance());
        }

        return requestManager;
    }

}
