package com.funlisten.business.search.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.search.contract.ZYSearchContract;
import com.funlisten.business.search.model.ZYSearchModel;
import com.funlisten.business.search.model.bean.ZYAudioAndAlbumInfo;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

/**
 * Created by gd on 2017/8/8.
 */

public class ZYSearchAudioPresenter extends ZYListDataPresenter<ZYSearchContract.AudioView,ZYSearchModel,ZYAudioAndAlbumInfo> implements ZYSearchContract.AudioPresenter {
    String type = ZYBaseModel.AUDIO_TYPE;
    String keyword;

    public ZYSearchAudioPresenter(ZYSearchContract.AudioView view, ZYSearchModel model,String keyword) {
        super(view, model);
        this.keyword = keyword;
    }

    public void search(String type,String keyword){
        this.type = type;
        this.keyword = keyword;
        loadData();
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getSearch(type,keyword),new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYAudioAndAlbumInfo>>>(){

            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYAudioAndAlbumInfo>> response) {
                super.onSuccess(response);
                success(response);

            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }

    @Override
    public void getAlbumDetail(final ZYAudio audio){
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getAlbumDetail(audio.albumId), new ZYNetSubscriber<ZYResponse<ZYAlbumDetail>>() {
            @Override
            public void onSuccess(ZYResponse<ZYAlbumDetail> response) {
                mView.loadAudio(response.data,audio);
                super.onSuccess(response);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }

    @Override
    public void search(String keyword){
        this.keyword = keyword;
        loadData();

    }
}
