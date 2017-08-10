package com.funlisten.business.album.view.viewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.utils.ZYScreenUtils;

import butterknife.OnClick;

/**
 * Created by gd on 2017/7/29.
 */

public class ZYAlbumFooterVH extends ZYBaseViewHolder {

    AlbumFooterListener footerListener;

    public ZYAlbumFooterVH(AlbumFooterListener footerListener) {
        this.footerListener = footerListener;
    }

    @Override
    public void updateView(Object data, int position) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_album_ft_item;
    }

    @OnClick({R.id.pay_tv})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.pay_tv:
                footerListener.onPay();
                break;
        }
    }

    @Override
    public void attachTo(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResId(), viewGroup, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ZYScreenUtils.dp2px(viewGroup.getContext(), 50));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);
        bindView(view);
        viewGroup.addView(getItemView());
    }

    public interface AlbumFooterListener {
        void onPay();
    }
}
