package com.funlisten.base.activity.picturePicker;


import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;


import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;

import butterknife.Bind;


public class ZYPictureVH extends ZYBaseViewHolder<ZYPicture> {

    @Bind(R.id.img_picture)
    ImageView mImgPicture;
    @Bind(R.id.img_check)
    ImageView mImgCheck;

    private boolean mIsSingle = false;
    private OnPictureSelectListener mOnPictureSelectListener;


    public ZYPictureVH(@NonNull OnPictureSelectListener onPictureSelectListener, boolean isSingle) {
        mOnPictureSelectListener = onPictureSelectListener;
        mIsSingle = isSingle;
    }

    @Override
    public void updateView(final ZYPicture data, final int position) {
        if (data != null) {
            ZYImageLoadHelper.getImageLoader().loadImage(mContext, mImgPicture, data.path, R.drawable.img_default_pic, R.drawable.img_default_pic);
            mImgCheck.setSelected(data.isSelected);

//            setMark(mImgPicture, data.isSelected);

            if (mIsSingle) {
                mImgCheck.setVisibility(View.GONE);
            } else {
                mImgCheck.setVisibility(View.VISIBLE);
                mImgCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImgCheck.setSelected(!data.isSelected);
                        setMark(mImgPicture, !data.isSelected);
                        mOnPictureSelectListener.onSelect(!data.isSelected, position);
                    }
                });
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_item_picture;
    }

    private void setMark(ImageView imageView, boolean isSelected) {
        if (imageView != null) {
            imageView.clearColorFilter();
            if (isSelected) {
                imageView.setColorFilter(new PorterDuffColorFilter(
                        ContextCompat.getColor(mContext, R.color.c1),
                        PorterDuff.Mode.SRC_OVER));
            } else {
                imageView.setColorFilter(new PorterDuffColorFilter(
                        ContextCompat.getColor(mContext, R.color.c10),
                        PorterDuff.Mode.SRC_OVER));
            }
        }
    }

    public interface OnPictureSelectListener {
        void onSelect(boolean isSelect, int position);
    }
}
