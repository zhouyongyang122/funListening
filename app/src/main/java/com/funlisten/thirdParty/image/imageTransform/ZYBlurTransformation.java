package com.funlisten.thirdParty.image.imageTransform;


import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.funlisten.utils.ZYGaussianBlurUtils;

public class ZYBlurTransformation implements Transformation<Bitmap> {

  private static int MAX_RADIUS = 25;
  private static int DEFAULT_DOWN_SAMPLING = 1;

  private Context mContext;
  private BitmapPool mBitmapPool;

  private int mRadius;
  private int mSampling;

  public ZYBlurTransformation(Context context) {
    this(context, Glide.get(context).getBitmapPool(), MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
  }

  public ZYBlurTransformation(Context context, BitmapPool pool) {
    this(context, pool, MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
  }

  public ZYBlurTransformation(Context context, BitmapPool pool, int radius) {
    this(context, pool, radius, DEFAULT_DOWN_SAMPLING);
  }

  public ZYBlurTransformation(Context context, int radius) {
    this(context, Glide.get(context).getBitmapPool(), radius, DEFAULT_DOWN_SAMPLING);
  }

  public ZYBlurTransformation(Context context, int radius, int sampling) {
    this(context, Glide.get(context).getBitmapPool(), radius, sampling);
  }

  public ZYBlurTransformation(Context context, BitmapPool pool, int radius, int sampling) {
    mContext = context.getApplicationContext();
    mBitmapPool = pool;
    mRadius = radius;
    mSampling = sampling;
  }

  @Override
  public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
    Bitmap source = resource.get();

    Bitmap bitmap = ZYGaussianBlurUtils.fastblur(source, 40);

    return BitmapResource.obtain(bitmap, mBitmapPool);
  }

  @Override
  public String getId() {
    return "ZYBlurTransformation(radius=" + mRadius + ", sampling=" + mSampling + ")";
  }
}
