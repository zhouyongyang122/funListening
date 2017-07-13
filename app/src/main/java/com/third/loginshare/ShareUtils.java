package com.third.loginshare;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;

import java.io.ByteArrayOutputStream;

public class ShareUtils
{
    //    private static final int THUMB_SIZE = 150;

    private static int MAX_SIZE = 32 * 1024;
    
    public static byte[] bmpToByteArray(Bitmap bmp){

        if (bmp != null){
            int quality = 50;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bmp.compress(CompressFormat.JPEG, quality, output);
//        bmp.recycle();
            while (output.toByteArray().length > MAX_SIZE && quality != 0) {
                output.reset();
                quality -= 10;
                bmp.compress(CompressFormat.JPEG, quality, output);
            }

            byte[] result = output.toByteArray();
            try
            {
                output.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return result;
        }

        return null;

    }
    
    //    public static Bitmap createBtimap(String avatarPath)
    //    {
    //        Bitmap bmp = BitmapFactory.decodeFile(avatarPath);
    //        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
    //        bmp.recycle();
    //        return thumbBmp;
    //    }
    
    public static Uri resourceToUri(Context context, int resID)
    {
        //   String avatarLocalPath = Environment.getExternalStorageDirectory().getPath() + "/ic_launcher.png";
        //   avatarLocalPath = Uri.parse("android.resource://com.fztech.funchat/raw/ic_launcher").toString();
        //   avatarLocalPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ic_launcher).toString();
        
        //   方式一：
        //   InputStream is = getResources().openRawResource(R.id.filename);
        //   方式二
        //   AssetManager am =  null ;  
        //   am = getAssets();  
        //   InputStream is = am.open("filename" );
        Uri uri =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + context.getResources().getResourcePackageName(resID) + '/'
                + context.getResources().getResourceTypeName(resID) + '/'
                + context.getResources().getResourceEntryName(resID));
        return uri;
    }
}
