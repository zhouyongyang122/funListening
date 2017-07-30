package com.funlisten.business.comment.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.album.view.viewHolder.ZYCommentItemVH;
import com.funlisten.business.comment.contract.ZYCommentContract;
import com.funlisten.business.comment.model.bean.ZYCommentHeaderInfo;
import com.funlisten.business.comment.view.viewholder.ZYCommentHeaderVH;
import com.funlisten.business.comment.view.viewholder.ZYSendVH;

import java.util.ArrayList;

/**
 * Created by gd on 2017/7/25.
 */

public class ZYCommentFragment extends ZYListDateFragment<ZYCommentContract.IPresenter,ZYComment> implements
        ZYCommentItemVH.CommentItemListener , ZYCommentContract.IView,ZYSendVH.SendMessageListener{

    ZYSendVH sendVH;
    ZYCommentHeaderVH  headerVH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRefreshRecyclerView.setRefreshEnable(false);
        headerVH = new ZYCommentHeaderVH();
        mAdapter.addHeader(headerVH);
        sendVH = new ZYSendVH(this);
        sendVH.attachTo(mRootView);
        sendVH.updateView(null,0);
        return view;
    }

    @Override
    protected void onItemClick(View view, int position) {

    }

    @Override
    protected ZYBaseViewHolder createViewHolder() {
        return new ZYCommentItemVH(ZYCommentFragment.this);
    }

    @Override
    public void suport(ZYComment comment) {
        mPresenter.suport(comment);
    }

    @Override
    public void suportCancle(ZYComment comment) {
        mPresenter.suportCancle(comment);
    }


    @Override
    public void showDatas(ArrayList<Object> datas) {
        showList(false);
    }

    @Override
    public void refreshList(int totalCount) {
        headerVH.updateView(new ZYCommentHeaderInfo(totalCount),0);
    }


    @Override
    public void sendMessage(String text) {
        mPresenter.addComment(text);
    }
}
