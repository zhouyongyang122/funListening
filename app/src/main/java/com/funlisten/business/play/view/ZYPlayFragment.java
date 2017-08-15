package com.funlisten.business.play.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.ZYAppConstants;
import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.event.ZYEventDowloadUpdate;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.base.view.ZYLoadingView;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.album.view.viewHolder.ZYCommentItemVH;
import com.funlisten.business.comment.activity.ZYCommentActivity;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.play.ZYPlayService;
import com.funlisten.business.play.contract.ZYPlayContract;
import com.funlisten.business.play.model.FZAudionPlayEvent;
import com.funlisten.business.play.model.ZYPlayManager;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.play.model.bean.ZYPlay;
import com.funlisten.business.play.presenter.ZYPlayPresenter;
import com.funlisten.business.play.view.viewHolder.ZYPlayActionBarVH;
import com.funlisten.business.play.view.viewHolder.ZYPlayAudiosVH;
import com.funlisten.business.play.view.viewHolder.ZYPlayHeaderVH;
import com.funlisten.service.downNet.down.ZYDownState;
import com.funlisten.service.downNet.down.ZYDownloadManager;
import com.funlisten.thirdParty.image.ZYIImageLoader;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.utils.SRShareUtils;
import com.funlisten.utils.ZYToast;
import com.third.loginshare.entity.ShareEntity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.funlisten.business.play.model.ZYPlayManager.*;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayFragment extends ZYBaseFragment<ZYPlayContract.IPresenter> implements ZYPlayHeaderVH.PlayHeaderListener, ZYPlayContract.IView, ZYPlayActionBarVH.PlayActionListener, ZYCommentItemVH.CommentItemListener, ZYPlayAudiosVH.PlayAudiosListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.textCollect)
    TextView textCollect;

    @Bind(R.id.textShare)
    TextView textShare;

    @Bind(R.id.textDown)
    TextView textDown;

    @Bind(R.id.textComment)
    TextView textComment;

    RelativeLayout mRootView;

    ZYPlayActionBarVH actionBarVH;

    ZYPlayHeaderVH headerVH;

    ZYPlayAudiosVH audiosVH;

    ZYBaseRecyclerAdapter<Object> adapter;

    ZYLoadingView loadingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (RelativeLayout) inflater.inflate(R.layout.zy_fragment_play, container, false);

        ButterKnife.bind(this, mRootView);

        actionBarVH = new ZYPlayActionBarVH(this);
        actionBarVH.attachTo(mRootView);

        audiosVH = new ZYPlayAudiosVH(this);
        audiosVH.attachTo(mRootView);
        audiosVH.hide();

        headerVH = new ZYPlayHeaderVH(this);
        adapter = new ZYBaseRecyclerAdapter<Object>(mPresenter.getComments()) {
            @Override
            public ZYBaseViewHolder<Object> createViewHolder(int type) {


                return new ZYCommentItemVH(ZYPlayFragment.this);
            }
        };
        adapter.addHeader(headerVH);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        loadingView = new ZYLoadingView(mActivity);
        loadingView.attach(mRootView);
        loadingView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.subscribe();
            }
        });
        return mRootView;
    }

    @OnClick({R.id.textComment, R.id.textCollect, R.id.textShare, R.id.textDown})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textDown:
                ZYAudio audio = mPresenter.getCurPlayAudio();
                ZYDownloadEntity downloadEntitys = ZYDownloadEntity.queryById(audio.id, audio.albumId);
                isCheckDown(downloadEntitys, audio);
                break;
            case R.id.textShare:
                ZYImageLoadHelper.getImageLoader().loadFromMediaStore(this, mPresenter.getAlbumDetail().coverUrl, new ZYIImageLoader.OnLoadLocalImageFinishListener() {
                    @Override
                    public void onLoadFinish(@Nullable final Bitmap bitmap) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShareEntity shareEntity = new ShareEntity();
                                shareEntity.avatarUrl = mPresenter.getAlbumDetail().coverUrl;
                                if (bitmap != null) {
                                    shareEntity.avatarBitmap = bitmap;
                                } else {
                                    shareEntity.avatarBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                                }
                                shareEntity.webUrl = ZYAppConstants.getShareUrl(mPresenter.getAlbumDetail().id);
                                shareEntity.title = mPresenter.getAlbumDetail().name;
                                shareEntity.text = mPresenter.getAlbumDetail().title;
                                new SRShareUtils(mActivity, shareEntity).share();
                            }
                        });
                    }
                });
                break;
            case R.id.textCollect:
                ZYAudio zyAudio = mPresenter.getCurPlayAudio();
                if (!textCollect.isSelected()) {
                    setCollect(true);
                    mPresenter.favorite(ZYBaseModel.AUDIO_TYPE, zyAudio.id);
                } else {
                    setCollect(false);
                    mPresenter.favoriteCancel(ZYBaseModel.AUDIO_TYPE, zyAudio.id);
                }
                break;
            case R.id.textComment:
                mActivity.startActivity(ZYCommentActivity.createIntent(mActivity, "audio", mPresenter.getCurPlayAudio().id + "", true));
                break;
        }
    }


    @Override
    public void refreshView() {
        ZYAudio audio = mPresenter.getCurPlayAudio();
        mPresenter.isFavorite("audio", audio.id);

        ZYDownloadEntity downloadEntity = ZYDownloadEntity.queryById(audio.id, audio.albumId);
        if (downloadEntity != null) refreshDown(downloadEntity);

        adapter.notifyDataSetChanged();
        ZYPlay play = new ZYPlay(mPresenter.getAlbumDetail(), mPresenter.getCurPlayAudio());
        headerVH.setUpdated(false);
        headerVH.updateView(play, 0);
        ZYPlayManager.getInstance().play(mPresenter.getCurPlayAudio(), mPresenter.getAudios(), mPresenter.getAlbumDetail());
    }

    @Override
    public void refreshComment() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setCollect(boolean isCollect) {
        if (isCollect) {
            Drawable drawable = getResources().getDrawable(R.drawable.icon_collect_pxx);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textCollect.setCompoundDrawables(null, drawable, null, null);
            textCollect.setSelected(true);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.tab_icon_collect_n);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textCollect.setCompoundDrawables(null, drawable, null, null);
            textCollect.setSelected(false);
        }
    }

    private void setTextDown(boolean isDown) {
        if (isDown) {
            Drawable drawable = getResources().getDrawable(R.drawable.icon_downloadi_pxx);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textDown.setCompoundDrawables(null, drawable, null, null);
            textDown.setSelected(false);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.tab_icon_download_n);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textDown.setCompoundDrawables(null, drawable, null, null);
            textDown.setSelected(true);
        }
    }

    private void isCheckDown(ZYDownloadEntity mDownloadEntity, ZYAudio audio) {
        if (mDownloadEntity != null) {
            if (mDownloadEntity.getState() == ZYDownState.FINISH) {
                ZYToast.show(mActivity, "已下载");
            } else if (mDownloadEntity.getState() == ZYDownState.ERROR) {
                textDown.setText("下载失败");
            } else if (mDownloadEntity.getState() == ZYDownState.PAUSE) {
                textDown.setText("下载暂停");
            } else if (mDownloadEntity.getState() == ZYDownState.DOWNING) {
                ZYToast.show(mActivity, "下载中");
            }
        } else {
            ZYDownloadEntity downloadEntity = ZYDownloadEntity.createEntityByAudio(mPresenter.getAlbumDetail(), audio);
            ZYDownloadManager.getInstance().addAudio(downloadEntity);
        }
    }

    private void refreshDown(ZYDownloadEntity mDownloadEntity) {
        if (mDownloadEntity != null) {
            if (mDownloadEntity.getState() == ZYDownState.FINISH) {
                textDown.setText("已下载");
                setTextDown(true);
            } else if (mDownloadEntity.getState() == ZYDownState.ERROR || mDownloadEntity.getState() == ZYDownState.PAUSE) {
                textDown.setText("下载失败");
                setTextDown(false);
            } else if (mDownloadEntity.getState() == ZYDownState.DOWNING) {
                textDown.setText("下载中");
                setTextDown(true);
            }
        } else {
            setTextDown(false);
            textDown.setText("下载");
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        mActivity.overridePendingTransition(0, R.anim.slide_down);
    }

    @Override
    public void onMoreActionPressed() {

    }

    @Override
    public void suport(ZYComment comment) {

    }

    @Override
    public void suportCancle(ZYComment comment) {

    }

    @Override
    public void moreComment() {
        mActivity.startActivity(ZYCommentActivity.createIntent(mActivity, "audio", mPresenter.getCurPlayAudio().id + "", true));
    }

    @Override
    public void showLoading() {
        super.showLoading();
        loadingView.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        loadingView.showNothing();
    }

    @Override
    public void showError() {
        super.showError();
        loadingView.showError();
    }

    @Override
    public void onPreClick(ZYPlay play) {
        ZYAudio audio = null;
        if (ZYPlayManager.getInstance().getPlayType() == ZYPlayService.PLAY_RANDOM_TYPE) {
            int position = new Random().nextInt(mPresenter.getAudios().size());
            audio = mPresenter.getAudios().get(position);

            ZYPlayPresenter playPresenter = (ZYPlayPresenter) mPresenter;//刷新界面
            playPresenter.refreshPlay(audio.id);

            mPresenter.setCurPlayAudio(audio);
            ZYPlayManager.getInstance().play(audio, mPresenter.getAudios(), mPresenter.getAlbumDetail());
            return;
        }
        int position = mPresenter.getAudios().indexOf(mPresenter.getCurPlayAudio());
        if (position >= 1) {
            audio = mPresenter.getAudios().get(--position);

            ZYPlayPresenter playPresenter = (ZYPlayPresenter) mPresenter;//刷新界面
            playPresenter.refreshPlay(audio.id);

            mPresenter.setCurPlayAudio(audio);
            ZYPlayManager.getInstance().play(audio, mPresenter.getAudios(), mPresenter.getAlbumDetail());
        } else {
            ZYToast.show(mActivity, "当前为第一集");
        }

        ZYAudio audios = mPresenter.getCurPlayAudio();
        ZYDownloadEntity downloadEntity = ZYDownloadEntity.queryById(audios.id, audios.albumId);
        refreshDown(downloadEntity);
    }

    @Override
    public void onNextClick(ZYPlay play) {
        ZYAudio audio = null;
        if (ZYPlayManager.getInstance().getPlayType() == ZYPlayService.PLAY_RANDOM_TYPE) {
            int position = new Random().nextInt(mPresenter.getAudios().size());
            audio = mPresenter.getAudios().get(position);

            ZYPlayPresenter playPresenter = (ZYPlayPresenter) mPresenter;//刷新界面
            playPresenter.refreshPlay(audio.id);

            mPresenter.setCurPlayAudio(audio);
            ZYPlayManager.getInstance().play(audio, mPresenter.getAudios(), mPresenter.getAlbumDetail());
            return;
        }
        int position = mPresenter.getAudios().indexOf(mPresenter.getCurPlayAudio());
        if (position < mPresenter.getAudios().size() - 1) {
            audio = mPresenter.getAudios().get(++position);

            ZYPlayPresenter playPresenter = (ZYPlayPresenter) mPresenter;//刷新界面
            playPresenter.refreshPlay(audio.id);

            mPresenter.setCurPlayAudio(audio);
            ZYPlayManager.getInstance().play(audio, mPresenter.getAudios(), mPresenter.getAlbumDetail());
        } else {
            ZYToast.show(mActivity, "已经是最后一集了");
        }
        ZYAudio audios = mPresenter.getCurPlayAudio();
        ZYDownloadEntity downloadEntity = ZYDownloadEntity.queryById(audios.id, audios.albumId);
        refreshDown(downloadEntity);
    }

    @Override
    public void onAudiosItemClick(int position) {
        mPresenter.setCurPlayAudio(mPresenter.getAudios().get(position));
        ZYPlayManager.getInstance().play(mPresenter.getAudios().get(position), mPresenter.getAudios(), mPresenter.getAlbumDetail());
    }

    @Override
    public void onAudiosViewClose() {
        headerVH.refreshPlayType();
    }

    @Override
    public void onPlayListClick() {
        audiosVH.updateView(mPresenter.getAudios(), mPresenter.getAudios().indexOf(mPresenter.getCurPlayAudio()));
    }

    @Override
    public void onPlayTypeClick() {

    }

    @Override
    public void onSubscribeClick(ZYAlbumDetail detail) {
        if (detail.isFavorite) {
            mPresenter.favoriteCancel(ZYBaseModel.ALBUM_TYPE, detail.id);
            detail.isFavorite = false;
        } else {
            mPresenter.favorite(ZYBaseModel.ALBUM_TYPE, detail.id);
            detail.isFavorite = true;
        }
        headerVH.updateSubscribeState(detail.isFavorite);
    }

    @Override
    public void refreshFavorite(boolean isFavorite) {
        headerVH.updateSubscribeState(isFavorite);
    }

    @Override
    public void onPlayOrPauseClick(ZYPlay play) {
        ZYPlayManager.getInstance().startOrPuase();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FZAudionPlayEvent playEvent) {
        if (playEvent != null) {
            if (playEvent.state == STATE_ERROR) {
                ZYToast.show(mActivity, "播放出错,请重新尝试!");
            } else if (playEvent.state == STATE_PREPARING) {
                showProgress();
            } else if (playEvent.state == STATE_PREPARED) {
                hideProgress();
            } else if (playEvent.state == STATE_PLAYING) {

            } else if (playEvent.state == STATE_PAUSED) {

            } else if (playEvent.state == STATE_NEED_BUY_PAUSED) {
                ZYToast.show(mActivity, "音频需要购买哦!");
            } else if (playEvent.state == STATE_BUFFERING_START) {

            } else if (playEvent.state == STATE_BUFFERING_END) {

            } else if (playEvent.state == STATE_PREPARING_NEXT) {
                showProgress();
            } else if (playEvent.state == STATE_COMPLETED) {

            }

            if (playEvent.audio != null) {
                mPresenter.setCurPlayAudio(playEvent.audio);
                actionBarVH.showTitle(playEvent.audio.title);
            }
            headerVH.refreshPlayState(playEvent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ZYEventDowloadUpdate dowloadUpdate) {
        if (dowloadUpdate.downloadEntity != null) {
            ZYAudio audio = mPresenter.getCurPlayAudio();
            if (dowloadUpdate.downloadEntity.getId().equals(ZYDownloadEntity.getEntityId(audio.id, audio.albumId))) {
                refreshDown((ZYDownloadEntity) dowloadUpdate.downloadEntity);
            }
        }
    }
}
