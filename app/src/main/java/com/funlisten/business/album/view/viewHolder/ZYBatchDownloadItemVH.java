package com.funlisten.business.album.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.event.ZYEventDowloadUpdate;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.downNet.down.ZYDownState;
import com.funlisten.utils.ZYDateUtils;
import com.funlisten.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYBatchDownloadItemVH extends ZYBaseViewHolder<ZYAudio> {

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

    @Bind(R.id.layoutDownload)
    RelativeLayout layoutDownload;

    ZYAudio mData;

    OnItemSelectAudio selectAudio;

    public ZYBatchDownloadItemVH(OnItemSelectAudio selectAudio) {
        this.selectAudio = selectAudio;
    }

    @Override
    public void updateView(ZYAudio data, int position) {
        if (data != null) {
            mData = data;
            textName.setText("第 " + mData.sort + " 期 | " + mData.title);
            textPlayNum.setText(mData.playCount + "");
            textTimeDay.setText(ZYDateUtils.getTimeString(mData.gmtCreate, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.YYMMDDHH));
            textTimeHours.setText(ZYDateUtils.getTimeString(mData.gmtCreate, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.HHMM24));
            layoutDownload.setTag(mData);
            imgDownload.setSelected(data.isSelect);
        }
    }


    @Override
    public int getLayoutResId() {
        return R.layout.gd_batch_down_item;
    }

    @OnClick({R.id.layoutDownload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutDownload:
                if (imgDownload.isSelected()) {
                    imgDownload.setSelected(false);
                } else {
                    imgDownload.setSelected(true);
                }
                ZYAudio audio = (ZYAudio) layoutDownload.getTag();
                if(audio != null){
                    selectAudio.onSelect(audio);
                }
                break;
        }
    }

    public interface OnItemSelectAudio{
        void onSelect(ZYAudio audio);
    }

}
