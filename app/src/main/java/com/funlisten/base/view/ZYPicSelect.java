package com.funlisten.base.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.funlisten.R;
import com.funlisten.ZYApplication;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYToast;
import com.funlisten.utils.ZYUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;

/**
 * Created by ZY on 17/3/21.
 */

public class ZYPicSelect implements View.OnClickListener {

    private final int PICK_FROM_CAMERA = 1;

    private final String PIC_NAME = "pic_select.jpg";

    private final String PIC_DIR = ZYApplication.CACHE_ROOT_DIR;

    private final String PIC_PATH = PIC_DIR + File.separator + PIC_NAME;

    private Dialog mDialog;

    private Activity mContext;

    protected Uri mImageCaptureUri;

    protected PicSelectListener mSelectListener;

    protected float maxWidth = 1000.0f;

    protected float maxHeight = 1000.0f;

    public ZYPicSelect(Activity context, PicSelectListener selectListener) {
        mContext = context;
        mDialog = new Dialog(context, R.style.SRDialogStyle);
        mSelectListener = selectListener;

        Window dialogWindow = mDialog.getWindow();
        dialogWindow.getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        mDialog.setCanceledOnTouchOutside(true);

        FrameLayout wrapLayout = new FrameLayout(context);
        wrapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mDialog.dismiss();
            }
        });

        View view = LayoutInflater.from(mContext).inflate(
                R.layout.sr_view_pic_sel_menu, null);
        view.findViewById(R.id.select_from_album).setOnClickListener(this);
        view.findViewById(R.id.take_photo).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        wrapLayout.addView(view, layoutParams);

        mDialog.setContentView(wrapLayout, new FrameLayout.LayoutParams(
                context.getResources().getDisplayMetrics().widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setMaxWidthAndHeight(float width, float height) {
        maxHeight = width;
        maxHeight = height;
    }

    public void showSelectDialog() {
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.select_from_album:
                    mDialog.dismiss();
                    if (ZYUtils.existSDCard()) {
                        ZYPicker.create().single().start(mContext);
                    } else {
                        ZYToast.show(mContext, "没有sdcard");
                    }
                    break;
                case R.id.take_photo:
                    mDialog.dismiss();
                    if (ZYUtils.existSDCard()) {
                        takePhoto();
                    } else {
                        ZYToast.show(mContext, "没有sdcard");
                    }
                    break;
                case R.id.cancel:
                    mDialog.dismiss();
                    break;
            }
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "onClick-error: " + e.getMessage());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case ZYPicker.REQUEST_PICKER:
                cropPic(ZYPicker.getSingleOutput(data));
                break;
            case PICK_FROM_CAMERA:
                cropPic(mImageCaptureUri);
                break;
            case UCrop.REQUEST_CROP:
                Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null && mSelectListener != null) {
                    mSelectListener.onPicSelected(resultUri);
                }
                break;
            default:
                break;
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File picDir = new File(PIC_DIR);
        if (!picDir.exists()) {
            picDir.mkdirs();
        }
        mImageCaptureUri = Uri.fromFile(new File(PIC_PATH));
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        try {
            mContext.startActivityForResult(intent, PICK_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
        }
    }

    private void cropPic(Uri uri) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setHideBottomControls(true);
        options.setShowCropGrid(false);
        options.setCompressionQuality(50);
        options.setStatusBarColor(mContext.getResources().getColor(R.color.c2));
        options.setToolbarColor(mContext.getResources().getColor(R.color.c2));
        options.setToolbarWidgetColor(mContext.getResources().getColor(R.color.white));
        UCrop crop = UCrop.of(uri, Uri.fromFile(new File(PIC_DIR, System.currentTimeMillis() + ".jpg")))
                .withAspectRatio(maxWidth, maxHeight)
                .withMaxResultSize((int) maxWidth, (int) maxHeight)
                .withOptions(options);
        crop.start(mContext);
    }

    public interface PicSelectListener {
        void onPicSelected(Uri uri);
    }
}
