package com.funlisten.business.album.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.utils.ZYDateUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYAudioItemVH extends ZYBaseViewHolder<ZYAudio> {

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textTimeDay)
    TextView textTimeDay;

    @Bind(R.id.textPlayNum)
    TextView textPlayNum;

    @Bind(R.id.textTimeHours)
    TextView textTimeHours;

    @Bind(R.id.imgDownload)
    ImageView imgDownload;

    ZYAudio mData;

    @Override
    public void updateView(ZYAudio data, int position) {
        if (data != null) {
            mData = data;
            textName.setText("第 " + mData.sort + " 期 | " + mData.title);
            textPlayNum.setText(mData.playCount + "");
            textTimeDay.setText(ZYDateUtils.getTimeString(mData.gmtCreate, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.YYMMDDHH));
            textTimeHours.setText(ZYDateUtils.getTimeString(mData.gmtCreate, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.HHMM24));
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_audio_item;
    }

    @OnClick({R.id.imgDownload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgDownload:
                break;
        }
    }
}
