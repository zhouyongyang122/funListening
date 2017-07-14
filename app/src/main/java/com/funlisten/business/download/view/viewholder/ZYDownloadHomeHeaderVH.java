package com.funlisten.business.download.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.event.ZYEventDowloadUpdate;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/12.
 */

public class ZYDownloadHomeHeaderVH extends ZYBaseViewHolder<ZYDownloadEntity> {

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textPublisher)
    TextView textPublisher;

    @Bind(R.id.textSize)
    TextView textSize;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    ZYDownloadEntity mData;

    public ZYDownloadHomeHeaderVH() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void updateView(ZYDownloadEntity data, int position) {
        if (data != null) {
            mData = data;
            mItemView.setVisibility(View.VISIBLE);
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, data.albumCoverUrl);
            textPublisher.setText("超级演说家: " + data.albumPublisher);
            textName.setText(data.audioName);
            updateProgress();
        }
    }

    private void updateProgress() {
        long total = mData.total;
        long currentSize = mData.current;
        float totalM = (float) total / 1024.0f;
        float currentSizeM = (float) currentSize / 1024.0f;
        textSize.setText(String.format("%.2fM", currentSizeM) + " / " + String.format("%.2fM", totalM));
        float progress = (float) currentSize / (float) total;
        progressBar.setProgress((int) progress);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_download_home_header;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ZYEventDowloadUpdate dowloadUpdate) {
        if (dowloadUpdate.downloadEntity != null) {
            if (dowloadUpdate.downloadEntity.getId().equals(mData.id)) {
                mData = (ZYDownloadEntity) dowloadUpdate.downloadEntity;
                updateProgress();
            } else {
                mData = (ZYDownloadEntity) dowloadUpdate.downloadEntity;
                updateView(mData, 0);
            }
        }
    }

    @Override
    public void unAttachTo() {
        super.unAttachTo();
        EventBus.getDefault().unregister(this);
    }
}
