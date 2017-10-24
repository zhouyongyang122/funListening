package com.funlisten.business.album.view.viewHolder;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.business.persondata.activity.ZYPersonDataActivity;
import com.funlisten.business.profile.activity.ZYProFlieActivity;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.utils.ZYToast;
import com.zzhoujay.markdown.parser.Line;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYAlbumHomeHeaderVH extends ZYBaseViewHolder<ZYAlbumDetail> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textSubTitle)
    TextView textSubTitle;

    @Bind(R.id.textCate)
    TextView textCate;

    @Bind(R.id.textAnchor)
    TextView textAnchor;

    @Bind(R.id.textPlayNum)
    TextView textPlayNum;

    @Bind(R.id.textSubscribe)
    TextView textSubscribe;

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textFans)
    TextView textFans;

    @Bind(R.id.textFollow)
    TextView textFollow;

    @Bind(R.id.layoutPrice)
    LinearLayout layoutPrice;

    @Bind(R.id.textPrice)
    TextView textPrice;

    @Bind(R.id.textPriceOld)
    TextView textPriceOld;

    ZYAlbumDetail mData;

    HeaderListener mListener;

    public ZYAlbumHomeHeaderVH(HeaderListener listener) {
        mListener = listener;
    }

    @Override
    public void updateView(ZYAlbumDetail data, int position) {
        if (data != null) {
            mData = data;
            textTitle.setText(mData.name);
            textSubTitle.setText(mData.title);
            textCate.setText("类别: " + mData.getCategoryNames());
            textAnchor.setText("主播: " + mData.publisher.nickname);
            textPlayNum.setText("播放: " + mData.playCount);

            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, mData.coverUrl);
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgAvatar, mData.publisher.avatarUrl,R.drawable.def_avatar,R.drawable.def_avatar);
            textName.setText(mData.publisher.nickname);
            textFans.setText(mData.publisher.fans + " 粉丝");

            updateFollowState();

            if (data.isNeedBuy()) {
                layoutPrice.setVisibility(View.VISIBLE);
                if (data.originPrice > 0) {
                    textPriceOld.setVisibility(View.VISIBLE);
                    textPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    textPriceOld.setText("¥" + data.originPrice + "");
                }
                textPrice.setText("¥" + data.sellPrice + "");
            }
        }
    }

    public void attachTo(ViewGroup viewGroup) {
        bindView(LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResId(), viewGroup, false));
        viewGroup.addView(getItemView());
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_album_home_header;
    }

    @OnClick({R.id.textSubscribe, R.id.imgAvatar, R.id.textFollow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textFollow:
                //关注
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mListener.onFollowClick(mData);
                break;
            case R.id.textSubscribe:
                //订阅
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mListener.onSubscribeClick(mData);
                break;
            case R.id.imgAvatar:
                //个人主页
                mContext.startActivity(ZYProFlieActivity.createIntent(mContext, mData.publisherId + ""));
                break;
        }
    }

    public void updateFollowState() {
        if (textFollow == null) {
            return;
        }
        if (mData.followSate != null && (mData.followSate.equals(ZYBaseModel.FOLLOW_NO_STATE) || mData.followSate.equals(ZYBaseModel.FOLLOW_BE_STATE))) {
            textFollow.setText("加关注");
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_attention_pre_n);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textFollow.setCompoundDrawables(null, drawable, null, null);
        } else {
            textFollow.setText("已关注");
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_attention_n);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textFollow.setCompoundDrawables(null, drawable, null, null);
        }
    }

    public void updateSubscribeState() {
        if (mData.isFavorite) {
            textSubscribe.setText("已订阅");
        } else {
            textSubscribe.setText("订阅");
        }
    }

    public interface HeaderListener {
        void onSubscribeClick(ZYAlbumDetail mData);

        void onFollowClick(ZYAlbumDetail mData);
    }
}
