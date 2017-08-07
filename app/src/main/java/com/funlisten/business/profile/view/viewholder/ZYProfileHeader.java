package com.funlisten.business.profile.view.viewholder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.business.photo.ZYPhoto;
import com.funlisten.business.profile.contract.ZYProfileFollowPhoto;
import com.funlisten.business.profile.model.bean.ZYProfileHeaderInfo;
import com.funlisten.thirdParty.image.ZYGlideImageLoader;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.utils.ZYScreenUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/16.
 */

public class ZYProfileHeader extends ZYBaseViewHolder<ZYProfileHeaderInfo> {

    @Bind(R.id.imgAvatar)
    ImageView imageHeader;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textFollows)
    TextView textFollows;

    @Bind(R.id.textFans)
    TextView textFans;

    @Bind(R.id.text_breviary)
    TextView textBreviary;

    @Bind(R.id.text_details)
    TextView details;

    @Bind(R.id.breviary_line)
    RelativeLayout breviary_line;

    @Bind(R.id.details_line)
    RelativeLayout details_line;

    @Bind(R.id.album_count)
    TextView album_count;

    @Bind(R.id.photo_count)
    TextView photoCount;

    @Bind(R.id.image_line)
    LinearLayout image_line;

    ZYProfileHeaderInfo mData;

    public static ZYProfileFollowPhoto followPhoto;

    @Override
    public void updateView(ZYProfileHeaderInfo data, int position) {
        if(data != null){
            mData = data;
        }
        if(mData != null && album_count != null) {
            showUserInfo(mData.user);
            if(mData.response.totalCount >0)
            showPhoto(mData.response.data);

            photoCount.setText("相册 ("+mData.response.totalCount+")");
            showAlbum(mData.totalCount);
        }
    }

    public void showUserInfo(ZYUser user) {
        ZYImageLoadHelper.getImageLoader().loadCircleImage(mContext,imageHeader,user.avatarUrl,R.drawable.def_avatar,R.drawable.def_avatar);
        textName.setText(user.nickname);
        textFollows.setText(user.follow+" 关注");
        textFans.setText(user.fans+"粉丝");
        textBreviary.setText(user.intro= TextUtils.isEmpty(user.intro) ?  "这个人很懒 什么都没写!":user.intro);
        details.setText(user.intro);
    }

    @OnClick({R.id.breviary_btn,R.id.details_btn,R.id.follow_tv,R.id.into_photo})
    public  void OnClick(View view){
        switch (view.getId()){
            case R.id.breviary_btn:
                breviary_line.setVisibility(View.GONE);
                details_line.setVisibility(View.VISIBLE);
                break;
            case R.id.details_btn:
                breviary_line.setVisibility(View.VISIBLE);
                details_line.setVisibility(View.GONE);
                break;
            case R.id.follow_tv:
                if(followPhoto != null) followPhoto.onFollow(mData.user.id);
                break;
            case R.id.into_photo:
                if(followPhoto != null) followPhoto.intoPhoto(mData.user.id);
                break;
        }
    }

    public void showPhoto(List<ZYPhoto> list) {
        for (ZYPhoto photo:list){
            ImageView view = createImage();
            image_line.addView(view);
            ZYImageLoadHelper.getImageLoader().loadImage(mContext,view,photo.photoUrl);
        }
    }

    public void showAlbum(int count) {
        album_count.setText("专辑("+count+")");
    }


    @Override
    public int getLayoutResId() {
        return R.layout.gd_profile_header;
    }

    public ImageView createImage() {
        // 获取屏幕宽度
        int W = ZYScreenUtils.getScreenWidth(mContext);
        // 设置图片大小
        int cricleRadius = (W-ZYScreenUtils.dp2px(mContext,38))/ 4;
        ImageView circleImageView = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cricleRadius, cricleRadius);
        int margin = ZYScreenUtils.dp2px(mContext,2);
        params.setMargins(margin,0,0,0);
        circleImageView.setLayoutParams(params);
        return circleImageView;
    }
}
