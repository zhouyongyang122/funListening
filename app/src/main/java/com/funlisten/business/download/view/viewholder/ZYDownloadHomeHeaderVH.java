package com.funlisten.business.download.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.event.ZYEventDowloadUpdate;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.activity.ZYDownloadingActivity;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.service.downNet.down.ZYDownState;
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

    @Bind(R.id.layoutRoot)
    LinearLayout layoutRoot;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textPublisher)
    TextView textPublisher;

    @Bind(R.id.textSize)
    TextView textSize;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    ZYDownloadEntity mData;

    static float SIZE_M = 1024.0f * 1024.0f;

    DownloadHomeHeaderListener listener;

    public ZYDownloadHomeHeaderVH(DownloadHomeHeaderListener listener) {
        EventBus.getDefault().register(this);
        this.listener = listener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(ZYDownloadingActivity.createIntent(mContext));
            }
        });
    }

    @Override
    public void updateView(ZYDownloadEntity data, int position) {
        if (data != null) {
            mData = data;
        }
        if (mData != null && textName != null) {
            layoutRoot.setVisibility(View.VISIBLE);
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, mData.getAlbumDetail().coverUrl);
            textPublisher.setText("超级演说家: " + mData.getAlbumDetail().publisher.nickname);
            textName.setText(mData.getAudio().title);
            updateProgress();
        } else if (layoutRoot != null) {
            layoutRoot.setVisibility(View.GONE);
        }
    }

    public void hideView(ZYDownloadEntity data) {
        if (layoutRoot != null) {
            mData = data;
            layoutRoot.setVisibility(View.GONE);
        }
    }

    private void updateProgress() {
        long total = mData.total;
        long currentSize = mData.current;
        float totalM = (float) total / SIZE_M;
        float currentSizeM = (float) currentSize / SIZE_M;
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
        if (dowloadUpdate.downloadEntity != null && dowloadUpdate.downloadEntity.getId().equals(mData.id)) {
            if (dowloadUpdate.downloadEntity.getState() == ZYDownState.FINISH) {
                mData = ZYDownloadEntity.queryNotFinishedFristAudio();
                updateView(mData, 0);
                if (mData == null) {
                    listener.onDownloadAllFinished();
                }
            } else {
                mData = (ZYDownloadEntity) dowloadUpdate.downloadEntity;
                updateProgress();
            }
        }
    }

    @Override
    public void unAttachTo() {
        super.unAttachTo();
        EventBus.getDefault().unregister(this);
    }

    public interface DownloadHomeHeaderListener {
        void onDownloadAllFinished();
    }
}
