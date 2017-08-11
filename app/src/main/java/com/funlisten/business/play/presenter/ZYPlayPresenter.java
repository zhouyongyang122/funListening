package com.funlisten.business.play.presenter;

import android.text.TextUtils;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.play.contract.ZYPlayContract;
import com.funlisten.business.play.model.ZYPlayManager;
import com.funlisten.business.play.model.ZYPlayModel;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.utils.ZYLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func3;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayPresenter extends ZYBasePresenter implements ZYPlayContract.IPresenter {

    ZYPlayContract.IView mView;

    ZYPlayModel mModel;

    int mAudioId;

    int mAlbumId;

    ArrayList<Object> mComments = new ArrayList<Object>();

    ArrayList<ZYAudio> mAudios = new ArrayList<ZYAudio>();

    ZYAlbumDetail mAlbumDetail;

    String mSortType = ZYAlbumModel.SORT_DESC;

    ZYAudio mCurPlayAudio;

    public ZYPlayPresenter(ZYPlayContract.IView iView, int mAlbumId, int mAudioId, String sortType) {
        mView = iView;
        mModel = new ZYPlayModel();
        mView.setPresenter(this);
        this.mAudioId = mAudioId;
        this.mAlbumId = mAlbumId;
        if (!TextUtils.isEmpty(sortType)) {
            mSortType = sortType;
        }
    }

    @Override
    public void subscribe() {
        mView.showLoading();
        Observable<ZYResponse<ZYListResponse<ZYComment>>> observable = Observable.zip(
                mModel.getAlbumDetail(mAlbumId), mModel.getComments("audio", mAudioId + "", 1, 5), mModel.getAudios(1, 1000, mAlbumId, mSortType), new Func3<ZYResponse<ZYAlbumDetail>, ZYResponse<ZYListResponse<ZYComment>>, ZYResponse<ZYListResponse<ZYAudio>>, ZYResponse<ZYListResponse<ZYComment>>>() {
                    @Override
                    public ZYResponse<ZYListResponse<ZYComment>> call(ZYResponse<ZYAlbumDetail> albumRes, ZYResponse<ZYListResponse<ZYComment>> commentsRes, ZYResponse<ZYListResponse<ZYAudio>> audiosRes) {
                        mAlbumDetail = albumRes.data;
                        mAudios.clear();
                        mAudios.addAll(audiosRes.data.data);
                        mCurPlayAudio = getAudioById();
                        return commentsRes;
                    }
                });
        mSubscriptions.add(ZYNetSubscription.subscription(observable, new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYComment>>>() {
            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYComment>> response) {
                if (response.data != null && response.data.data != null && response.data.data.size() > 0) {
                    mComments.addAll(response.data.data);
                } else {
                    mComments.add(new ZYComment(-1));
                }
                mView.refreshView();
                mView.hideLoading();
            }

            @Override
            public void onFail(String message) {
                ZYDownloadEntity entity = ZYDownloadEntity.queryById(mAudioId, mAlbumId);
                if (entity.isDowloaded()) {
                    mAlbumDetail = entity.getAlbumDetail();
                    List<ZYDownloadEntity> entities = ZYDownloadEntity.queryAlbumDownloadedAudios(mAlbumId);
                    List<ZYAudio> audios = new ArrayList<ZYAudio>();
                    for (ZYDownloadEntity audio : entities) {
                        audios.add(audio.getAudio());
                    }
                    if (audios == null || audios.size() <= 0) {
                        mView.showError();
                        return;
                    }
                    mAudios.addAll(audios);
                    mCurPlayAudio = getAudioById();
                    mView.refreshView();
                    mView.hideLoading();
                } else {
                    mView.showError();
                }
            }
        }));
    }

    @Override
    public void isFavorite(String type, int objectId) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.isFavorite(type, objectId), new ZYNetSubscriber<ZYResponse<Boolean>>() {
            @Override
            public void onSuccess(ZYResponse<Boolean> response) {
                super.onSuccess(response);
                mView.setCollect(response.data);
            }

            @Override
            public void onFail(String message) {
            }
        }));
    }

    public void favorite(String type, int objectId) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.favorite(objectId + "", type), new ZYNetSubscriber<ZYResponse<Object>>() {
            @Override
            public void onSuccess(ZYResponse<Object> response) {
                ZYLog.e(response.data.toString());
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }


    public void favoriteCancel(String type, int objectId) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.favoriteCancel(objectId + "", type), new ZYNetSubscriber<ZYResponse<Object>>() {
            @Override
            public void onSuccess(ZYResponse<Object> response) {
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }

    public void refreshPlay(int album, int audio) {
        if (mAlbumDetail == null || album != mAlbumDetail.id || mCurPlayAudio.id != audio) {
            ZYPlayManager.getInstance().puase();
            this.mAudioId = audio;
            this.mAlbumId = album;
            subscribe();
        }
    }

    public void changeSory() {
        if (mSortType == ZYAlbumModel.SORT_DESC) {
            //当前为降序
            mSortType = ZYAlbumModel.SORT_ASC;
        } else {
            mSortType = ZYAlbumModel.SORT_DESC;
        }
        Collections.reverse(mAudios);
    }

    ZYAudio getAudioById() {
        if (mAudios.size() > 0) {
            for (ZYAudio audio : mAudios) {
                if (audio.id == mAudioId) {
                    return audio;
                }
            }
            return mAudios.get(0);
        }
        return null;
    }

    public ArrayList<ZYAudio> getAudios() {
        return mAudios;
    }

    public void setCurPlayAudio(ZYAudio audio) {
        mCurPlayAudio = audio;
    }

    public List<Object> getComments() {
        return mComments;
    }

    public ZYAlbumDetail getAlbumDetail() {
        return mAlbumDetail;
    }

    public ZYAudio getCurPlayAudio() {
        return mCurPlayAudio;
    }
}
