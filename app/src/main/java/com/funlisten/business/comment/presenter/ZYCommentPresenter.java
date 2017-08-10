package com.funlisten.business.comment.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.comment.contract.ZYCommentContract;
import com.funlisten.business.comment.model.ZYCommentModel;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

/**
 * Created by Administrator on 2017/7/12.
 */

public class ZYCommentPresenter extends ZYListDataPresenter<ZYCommentContract.IView,ZYCommentModel,ZYComment> implements ZYCommentContract.IPresenter {
    String type;
    String objectId;

    public ZYCommentPresenter(ZYCommentContract.IView view, ZYCommentModel model,String type, String objectId) {
        super(view, model);
        this.type = type;
        this.objectId = objectId;
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getComments(type,objectId,mPageIndex,mRows),new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYComment>>>(){
            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYComment>> response) {
                super.onSuccess(response);
                mView.refreshList(response.data.totalCount);
                success(response);
            }

            @Override
            public void onFail(String message) {
              fail(message);
            }
        }));
    }

    @Override
    public void suport(ZYComment comment) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.suport(comment.id + "", ZYBaseModel.COMMENT_TYPE), new ZYNetSubscriber<ZYResponse>() {
            @Override
            public void onSuccess(ZYResponse response) {
//                mView.showDatas(mDatas);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }

    @Override
    public void suportCancle(ZYComment comment) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.suportCanlce(comment.id + "", ZYBaseModel.COMMENT_TYPE), new ZYNetSubscriber<ZYResponse>() {
            @Override
            public void onSuccess(ZYResponse response) {
//                mView.showDatas(mDatas);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }

    @Override
    public void addComment(String content) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.addComment(type,objectId,content), new ZYNetSubscriber<ZYResponse<ZYComment>>() {

            @Override
            public void onSuccess(ZYResponse<ZYComment> response) {
                super.onSuccess(response);
                loadData();
//                mView.showList(true);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }
}
