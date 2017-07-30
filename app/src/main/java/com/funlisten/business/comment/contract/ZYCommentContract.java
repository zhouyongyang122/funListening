package com.funlisten.business.comment.contract;

import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.album.model.bean.ZYComment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/12.
 */

public interface ZYCommentContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
        void showDatas(ArrayList<Object> datas);
        void refreshList(int totalCount);
    }

    interface IPresenter extends ZYListDataContract.Presenter<ZYComment> {
        void suport(ZYComment comment);
        void suportCancle(ZYComment comment);
        void addComment(String content);
    }
}
