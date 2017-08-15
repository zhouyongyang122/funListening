package com.funlisten.business.main.view.viewHolder;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.ZYApplication;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.dailylisten.activity.ZYDailyListenActivity;
import com.funlisten.business.play.activity.ZYPlayActivity;
import com.funlisten.business.search.view.WarpLinearLayout;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.business.main.model.bean.ZYHome;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/5/24.
 */

public class ZYHomeDayListenVH extends ZYBaseViewHolder<ZYHome.DayListening> implements View.OnClickListener {

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.layoutAudio)
    LinearLayout layoutAudio;

    ZYHome.DayListening mData;

    int albumId;

    @Override
    public void updateView(ZYHome.DayListening data, int position) {
        if (data != null) {
            mData = data;
            albumId = data.id;
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, mData.imageUrl);
            textTitle.setText(mData.name);
            layoutAudio.removeAllViews();
            if (mData.everyDayAudioListeningDtoList != null) {
                for (ZYHome.DayListenAudio listenAudio : mData.everyDayAudioListeningDtoList) {
                    TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.zy_view_home_day_listen_audio, layoutAudio, false);
                    view.setTag(listenAudio.id + "");
                    view.setText(listenAudio.title);
                    view.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    view.setOnClickListener(this);
                    layoutAudio.addView(view);
                }
            }
        }
    }

    @OnClick({R.id.layoutTitle})
    public void onClick(View v) {
        if (v.getId() == R.id.layoutTitle) {
            Intent intent = new Intent(ZYApplication.getInstance().getCurrentActivity(), ZYDailyListenActivity.class);
            intent.putExtra("albumId", albumId);
            ZYApplication.getInstance().getCurrentActivity().startActivity(intent);
            return;
        }
        try {
            int audioId = Integer.parseInt(v.getTag().toString());
            //跳转到音频详情页
            ZYPlayActivity.toPlayActivity(ZYApplication.getInstance().getCurrentActivity(), albumId, audioId);
        } catch (Exception e) {

        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_home_daylisten;
    }

}
