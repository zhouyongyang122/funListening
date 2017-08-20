package com.funlisten.business.profile.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/16.
 */

public class ZYProfileVH extends ZYBaseViewHolder<ZYAlbumDetail> {

    @Bind(R.id.coverUrl)
    ImageView coverUrl;

    @Bind(R.id.pay_icon)
    ImageView payIcon;

    @Bind(R.id.pay_tv)
    TextView payTv;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.textSubTitle)
    TextView textSubTitle;

    @Bind(R.id.play_count)
    TextView playCount;

    @Bind(R.id.audioCount)
    TextView audioCount;

    @Bind(R.id.into_album_detail)

    LinearLayout intoDetail;

    public static boolean totalCount;

    @Override
    public void updateView(ZYAlbumDetail data, int position) {
        if (totalCount){
            mItemView.setVisibility(View.GONE);
            return;
        }
        ZYImageLoadHelper.getImageLoader().loadImage(mContext,coverUrl,data.coverUrl);
        showPay("paid".equals(data.costType));
        title.setText(data.name);
        textSubTitle.setText(data.title);
        playCount.setText(data.playCount+"");
        audioCount.setText("更新至"+data.audioCount+"集");
        intoDetail.setTag(data);
    }

    @OnClick({R.id.into_album_detail})
    public void OnClick(View view){

        switch (view.getId()){
            case R.id.into_album_detail://进入album详情页
                ZYAlbumDetail data = (ZYAlbumDetail) view.getTag();
                break;
        }

    }

    private void showPay(boolean isShow){
        if(isShow){
            payIcon.setVisibility(View.VISIBLE);
            payTv.setVisibility(View.VISIBLE);
        }else {
            payIcon.setVisibility(View.GONE);
            payTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_profile_item;
    }
}
