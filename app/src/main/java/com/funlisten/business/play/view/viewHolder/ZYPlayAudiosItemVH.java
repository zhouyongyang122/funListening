package com.funlisten.business.play.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.play.model.bean.ZYAudio;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayAudiosItemVH extends ZYBaseViewHolder<ZYAudio> {

    @Bind(R.id.imgPlayIcon)
    ImageView imgPlayIcon;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.imgDownload)
    ImageView imgDownload;

    ZYAudio mData;

    @Override
    public void updateView(ZYAudio data, int position) {
        if (data != null) {
            mData = data;
            textTitle.setText("第 " + mData.sort + " 期 | " + mData.title);
            if (mData.isPlaying) {
                imgPlayIcon.setVisibility(View.VISIBLE);
            } else {
                imgPlayIcon.setVisibility(View.GONE);
            }

            imgDownload.setSelected(false);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_play_audios_item;
    }

    @OnClick({R.id.imgDownload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgDownload:
                break;
        }
    }
}
