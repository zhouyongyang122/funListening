package com.funlisten.business.main.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.business.main.model.bean.ZYHome;
import com.funlisten.utils.ZYScreenUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/5/24.
 */

public class ZYHomeModulItemVH extends ZYBaseViewHolder<ZYHome.ModuleItem> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.textPay)
    TextView textPay;

    @Bind(R.id.textTitle)
    TextView textTitle;

    ZYHome.ModuleItem mData;

    @Override
    public void findView(View view) {
        super.findView(view);

        int width = (ZYScreenUtils.getScreenWidth(mContext) - ZYScreenUtils.dp2px(mContext, 30)) / 3;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgBg.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        imgBg.setLayoutParams(layoutParams);
    }

    @Override
    public void updateView(ZYHome.ModuleItem data, int position) {
        if (data != null) {
            mData = data;
            if (position % 3 == 0) {
                mItemView.setPadding(0, 0, ZYScreenUtils.dp2px(mContext, 7), 0);
            } else if ((position + 1) % 3 == 0) {
                mItemView.setPadding(ZYScreenUtils.dp2px(mContext, 7), 0, 0, 0);
            } else {
                mItemView.setPadding(ZYScreenUtils.dp2px(mContext, 3), 0, ZYScreenUtils.dp2px(mContext, 3), 0);
            }
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, mData.coverUrl);
            textTitle.setText("没有提供标题");
            textPay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_home_modul_item;
    }
}
