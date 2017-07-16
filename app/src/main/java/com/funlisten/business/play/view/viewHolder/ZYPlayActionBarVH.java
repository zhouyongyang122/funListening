package com.funlisten.business.play.view.viewHolder;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.utils.ZYStatusBarUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayActionBarVH extends ZYBaseViewHolder<Object> {

    @Bind(R.id.layoutInfo)
    LinearLayout layoutInfo;

    @Bind(R.id.imgBack)
    ImageView imgBack;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.layoutPlay)
    LinearLayout layoutPlay;

    @Bind(R.id.textPlay)
    TextView textPlay;

    @Bind(R.id.imgShare)
    ImageView imgShare;

    @Bind(R.id.imgMore)
    ImageView imgMore;

    PlayActionListener listener;

    public ZYPlayActionBarVH(PlayActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutInfo.getLayoutParams();
            layoutParams.topMargin = ZYStatusBarUtils.getStatusBarHeight(mContext);
        }
    }

    @Override
    public void updateView(Object data, int position) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_play_aciton_bar;
    }

    @OnClick({R.id.imgBack, R.id.textPlay, R.id.imgShare, R.id.imgMore})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                listener.onBackPressed();
                break;
            case R.id.textPlay:
                listener.onPlayPressed();
                break;
            case R.id.imgShare:
                listener.onSharePressed();
                break;
            case R.id.imgMore:
                listener.onMoreActionPressed();
                break;
        }
    }

    public interface PlayActionListener {
        void onBackPressed();

        void onPlayPressed();

        void onSharePressed();

        void onMoreActionPressed();
    }
}
