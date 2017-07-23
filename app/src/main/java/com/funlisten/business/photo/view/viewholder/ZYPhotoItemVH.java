package com.funlisten.business.photo.view.viewholder;

import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.photo.ZYPhoto;
import com.funlisten.business.photo.contract.ZYPhotoSelect;
import com.funlisten.thirdParty.image.ZYGlideImageLoader;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.utils.ZYScreenUtils;

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
    ImageView imageBtn;

    public static ZYPhotoSelect photoSelect;
    public static boolean isEdit =false;

    @Override
    public void updateView(ZYPhoto data, int position) {
        ZYImageLoadHelper.getImageLoader().loadImage(this,photoImage,data.photoUrl,R.color.c1,R.color.c1);
        if(isEdit) imgLine.setVisibility(View.VISIBLE);
        else imgLine.setVisibility(View.GONE);
        imageBtn.setTag(data);
    }

    @OnClick({R.id.image_btn})
    public void onClick(View view){
        ZYPhoto data = (ZYPhoto) view.getTag();
        if(photoSelect != null){
            isCheck(data);
            photoSelect.onSelect(data);
        }
    }

    private void isCheck(ZYPhoto data){
        if (data.isSelect){
            imageBtn.setImageResource(R.drawable.nav_btn_paused_n);
        }else imageBtn.setImageResource(R.drawable.xiangce_xuanzhong_s);
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        RelativeLayout.LayoutParams layoutParams = ( RelativeLayout.LayoutParams)photoImage.getLayoutParams();
        layoutParams.height = (ZYScreenUtils.getScreenWidth(mContext)-ZYScreenUtils.dp2px(mContext,5))/4;
        photoImage.setLayoutParams(layoutParams);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_photo_item;
    }
}
