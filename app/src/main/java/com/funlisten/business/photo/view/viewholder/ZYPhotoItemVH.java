package com.funlisten.business.photo.view.viewholder;

import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.photo.ZYPhoto;
import com.funlisten.business.photo.contract.ZYPhotoSelect;
import com.funlisten.thirdParty.image.ZYGlideImageLoader;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/15.
 */

public class ZYPhotoItemVH extends ZYBaseViewHolder<ZYPhoto> {

    @Bind(R.id.photo_image)
    ImageView photoImage;

    @Bind(R.id.img_selected_line)
    LinearLayout imgLine;

    @Bind(R.id.image_btn)
    Button imageBtn;

    public static ZYPhotoSelect photoSelect;

    @Override
    public void updateView(ZYPhoto data, int position) {
        new ZYGlideImageLoader().loadImage(mContext,photoImage,data.photoUrl);
        if(data.isEdit) imgLine.setVisibility(View.VISIBLE);
        else imgLine.setVisibility(View.GONE);
        imageBtn.setTag(data);
    }

    @OnClick({R.id.image_btn})
    public void onClick(View view){
        ZYPhoto data = (ZYPhoto) view.getTag();
        if(photoSelect != null){
            photoSelect.onSelect(data);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_photo_item;
    }
}
