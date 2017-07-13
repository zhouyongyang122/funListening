package com.funlisten.thirdParty.image;

public class ZYImageLoadHelper {

    public static final int CENTER_CROP = 0;
    public static final int FIT_CENTER  = 1;

    private static ZYIImageLoader sImageLoader;

    public static ZYIImageLoader getImageLoader(){
        if (sImageLoader == null){
            sImageLoader = new ZYGlideImageLoader();
        }
        sImageLoader.setFitType(CENTER_CROP);
        sImageLoader.setWidth(0);
        sImageLoader.setHeight(0);
        sImageLoader.setColorFilter(0);
        return sImageLoader;
    }

}
