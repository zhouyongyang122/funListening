package com.funlisten.business.play.presenter;

import android.text.TextUtils;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.play.contract.ZYPlayContract;
import com.funlisten.business.play.model.ZYPLayManager;
import com.funlisten.business.play.model.ZYPlayModel;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.play.model.bean.ZYPlay;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import rx.Observable;
import rx.functions.Func3;
import rx.functions.Func4;

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
                mModel.getAlbumDetail(mAlbumId), mModel.getComments(mAlbumId + "", 1, 5), mModel.getAudios(1, 1000, mAlbumId, mSortType), new Func3<ZYResponse<ZYAlbumDetail>, ZYResponse<ZYListResponse<ZYComment>>, ZYResponse<ZYListResponse<ZYAudio>>, ZYResponse<ZYListResponse<ZYComment>>>() {
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
                mView.showError();
            }
        }));
    }

    public void refreshPlay(int album, int audio) {
        if (album != mAlbumDetail.id || mCurPlayAudio.id != audio) {
            ZYPLayManager.getInstance().puase();
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
        for (ZYAudio audio : mAudios) {
            if (audio.id == mAudioId) {
                return audio;
            }
        }
        return mAudios.get(0);
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
