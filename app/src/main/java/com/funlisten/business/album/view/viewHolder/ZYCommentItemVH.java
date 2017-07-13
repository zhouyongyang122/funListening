package com.funlisten.business.album.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYCommentItemVH extends ZYBaseViewHolder<Object> {

    @Bind(R.id.layoutInfo)
    LinearLayout layoutInfo;

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textTime)
    TextView textTime;

    @Bind(R.id.textSuport)
    TextView textSuport;

    @Bind(R.id.textDesc)
    TextView textDesc;

    @Bind(R.id.textMore)
    TextView textMore;

    ZYComment mData;

    CommentItemListener mListener;

    public ZYCommentItemVH(CommentItemListener listener) {
        mListener = listener;
    }

    @Override
    public void updateView(Object obj, int position) {
        if (obj != null && obj instanceof ZYComment) {
            mData = (ZYComment) obj;
            if(mData.id < 0){
                textMore.setVisibility(View.VISIBLE);
                layoutInfo.setVisibility(View.GONE);
                return;
            }
            textMore.setVisibility(View.GONE);
            layoutInfo.setVisibility(View.VISIBLE);
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, mData.user.avatarUrl);
            textName.setText(mData.user.nickname);
            textTime.setText(mData.gmtCreate);
            textSuport.setText(mData.likeCount + "");
            textDesc.setText(mData.content);
        }
    }

    @OnClick({R.id.textSuport, R.id.imgAvatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgAvatar:
                break;
            case R.id.textSuport:
                mListener.suport(mData);
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_comment_item;
    }

    public interface CommentItemListener {
        void suport(ZYComment comment);
    }
}
