package com.funlisten.business.album.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.activity.picturePicker.ZYAlbum;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.downNet.down.ZYDownloadManager;
import com.funlisten.utils.ZYDateUtils;
import com.funlisten.utils.ZYToast;

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

    ZYDownloadEntity mDownloadEntity;

    AudioItemListener listener;

    public ZYAudioItemVH(AudioItemListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView(ZYAudio data, int position) {
        if (data != null) {
            mData = data;
            textName.setText("第 " + mData.sort + " 期 | " + mData.title);
            textPlayNum.setText(mData.playCount + "");
            textTimeDay.setText(ZYDateUtils.getTimeString(mData.gmtCreate, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.YYMMDDHH));
            textTimeHours.setText(ZYDateUtils.getTimeString(mData.gmtCreate, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.HHMM24));
            mDownloadEntity = ZYDownloadEntity.queryById(mData.id, mData.albumId);
            if (mDownloadEntity != null) {
                imgDownload.setSelected(true);
            } else {
                imgDownload.setSelected(false);
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_audio_item;
    }

    @OnClick({R.id.layoutDownload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutDownload:
                if (!imgDownload.isSelected()) {
                    ZYToast.show(mContext, "开始下载!");
                    listener.onDownloadClick(mData);
                    imgDownload.setSelected(true);
                } else {
                    ZYToast.show(mContext, "已经下载!");
                }
                break;
        }
    }

    public interface AudioItemListener {
        void onDownloadClick(ZYAudio audio);
    }
}
