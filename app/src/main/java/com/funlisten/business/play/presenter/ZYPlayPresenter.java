package com.funlisten.business.play.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBasePresenter;
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
import java.util.List;
import java.util.Objects;

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

    ArrayList<Object> comments = new ArrayList<Object>();

    ZYAlbumDetail albumDetail;

    ZYAudio audio;

    boolean isNewPlay;

    public ZYPlayPresenter(ZYPlayContract.IView iView, int mAlbumId, int mAudioId) {
        mView = iView;
        mModel = new ZYPlayModel();
        mView.setPresenter(this);
        this.mAudioId = mAudioId;
        this.mAlbumId = mAlbumId;
        isNewPlay = ZYPLayManager.getInstance().getPlay() == null;
    }

    @Override
    public void subscribe() {
        if (isNewPlay) {
            mView.showLoading();
            Observable<ZYResponse<ZYListResponse<ZYComment>>> observable = Observable.zip(
                    mModel.getAlbumDetail(mAlbumId), mModel.getAudio(mAudioId + ""), mModel.getComments(mAlbumId + "", 1, 5), new Func3<ZYResponse<ZYAlbumDetail>, ZYResponse<ZYAudio>, ZYResponse<ZYListResponse<ZYComment>>, ZYResponse<ZYListResponse<ZYComment>>>() {
                        @Override
                        public ZYResponse<ZYListResponse<ZYComment>> call(ZYResponse<ZYAlbumDetail> albumDetailZYResponse, ZYResponse<ZYAudio> audioZYResponse, ZYResponse<ZYListResponse<ZYComment>> zyListResponseZYResponse) {
                            if (albumDetailZYResponse == null || audioZYResponse == null) {
                                return null;
                            }
                            albumDetail = albumDetailZYResponse.data;
                            audio = audioZYResponse.data;
                            if (albumDetail == null || audioZYResponse == null) {
                                return null;
                            }
                            return zyListResponseZYResponse;
                        }
                    }
            );
            mSubscriptions.add(ZYNetSubscription.subscription(observable, new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYComment>>>() {
                @Override
                public void onSuccess(ZYResponse<ZYListResponse<ZYComment>> response) {
                    if (response == null) {
                        onFail("");
                        return;
                    }
                    if (response.data != null && response.data.data != null && response.data.data.size() > 0) {
                        comments.addAll(response.data.data);
                    } else {
                        comments.add(new ZYComment(-1));
                    }
                    mView.refreshView(isNewPlay);
                    mView.hideLoading();
                }

                @Override
                public void onFail(String message) {
                    mView.showError();
                }
            }));
        } else {
            comments.addAll(ZYPLayManager.getInstance().getComments());
            ZYPlay play = ZYPLayManager.getInstance().getPlay();
            albumDetail = play.albumDetail;
            audio = play.audio;
            mView.refreshView(false);
        }
    }

    public List<Object> getComments() {
        return comments;
    }

    public ZYAlbumDetail getAlbumDetail() {
        return albumDetail;
    }

    public ZYAudio getAudio() {
        return audio;
    }
}
