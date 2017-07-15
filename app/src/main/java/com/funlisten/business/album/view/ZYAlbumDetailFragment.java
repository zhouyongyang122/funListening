package com.funlisten.business.album.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.mvp.ZYBaseRecyclerFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.contract.ZYAlbumDetailContract;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYAlbumTitle;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.album.view.viewHolder.ZYAlbumDetailItemVH;
import com.funlisten.business.album.view.viewHolder.ZYAlbumDetailTitleVH;
import com.funlisten.business.album.view.viewHolder.ZYCommentItemVH;
import com.funlisten.utils.ZYScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/7/5.
 */

public class ZYAlbumDetailFragment extends ZYBaseRecyclerFragment<ZYAlbumDetailContract.IPresenter> implements ZYAlbumDetailContract.IView, ZYCommentItemVH.CommentItemListener {

    ZYBaseRecyclerAdapter<Object> mAdapter;

    static final int ADAPTER_TYPE_TITLE = 0;
    static final int ADAPTER_TYPE_DESC = 1;
    static final int ADAPTER_TYPE_COMMENT = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);


        mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRefreshRecyclerView.setLoadMoreEnable(false);
        mRefreshRecyclerView.setRefreshEnable(false);
        mAdapter = new ZYBaseRecyclerAdapter<Object>(mPresenter.getDatas()) {

            @Override
            public int getItemViewType(int position) {
                int type = super.getItemViewType(position);
                if (type == ZYBaseRecyclerAdapter.TYPE_NORMAL) {
                    if (getItem(position) instanceof ZYAlbumTitle) {
                        return ADAPTER_TYPE_TITLE;
                    } else if (getItem(position) instanceof ZYAlbumDetail.Detail) {
                        return ADAPTER_TYPE_DESC;
                    } else if (getItem(position) instanceof ZYComment) {
                        return ADAPTER_TYPE_COMMENT;
                    }
                }
                return type;
            }

            @Override
            public ZYBaseViewHolder<Object> createViewHolder(int type) {
                if (ADAPTER_TYPE_TITLE == type) {
                    return new ZYAlbumDetailTitleVH();
                } else if (ADAPTER_TYPE_DESC == type) {
                    return new ZYAlbumDetailItemVH();
                } else if (ADAPTER_TYPE_COMMENT == type) {
                    return new ZYCommentItemVH(ZYAlbumDetailFragment.this);
                }
                return new ZYAlbumDetailTitleVH();
            }
        };
        mRefreshRecyclerView.setAdapter(mAdapter);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRefreshRecyclerView.getLoadingView().getView().getLayoutParams();
        params.height = ZYScreenUtils.dp2px(mActivity, 160);
        params.topMargin = ZYScreenUtils.dp2px(mActivity, 40);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mRefreshRecyclerView.getLoadingView().getView().setLayoutParams(params);

        mRefreshRecyclerView.getLoadingView().setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.subscribe();
            }
        });

        return view;
    }

    public void loadComments(List<ZYAlbumDetail.Detail> details) {
        mPresenter.loadComments(details);
    }

    @Override
    public void showDatas(ArrayList<Object> datas) {
        showList(false);
    }

    @Override
    public void suport(ZYComment comment) {
        mPresenter.suport(comment);
    }

    @Override
    public void suportCancle(ZYComment comment) {
        mPresenter.suportCancle(comment);
    }
}
