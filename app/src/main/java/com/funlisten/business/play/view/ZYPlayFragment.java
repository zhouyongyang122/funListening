package com.funlisten.business.play.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.album.view.viewHolder.ZYCommentItemVH;
import com.funlisten.business.play.presenter.ZYPlayPresenter;
import com.funlisten.business.play.view.viewHolder.ZYPlayActionBarVH;
import com.funlisten.business.play.view.viewHolder.ZYPlayAudiosVH;
import com.funlisten.business.play.view.viewHolder.ZYPlayHeaderVH;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayFragment extends ZYBaseFragment<ZYPlayPresenter> implements ZYPlayActionBarVH.PlayActionListener, ZYCommentItemVH.CommentItemListener {

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

    ZYPlayActionBarVH actionBarVH;

    ZYPlayHeaderVH headerVH;

    ZYPlayAudiosVH audiosVH;

    ZYBaseRecyclerAdapter<Object> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout) inflater.inflate(R.layout.zy_fragment_play, container, false);
        actionBarVH = new ZYPlayActionBarVH(this);
        actionBarVH.attachTo(rootView);

        headerVH = new ZYPlayHeaderVH();
        headerVH.attachTo(rootView);

        audiosVH = new ZYPlayAudiosVH();

        adapter = new ZYBaseRecyclerAdapter<Object>() {
            @Override
            public ZYBaseViewHolder<Object> createViewHolder(int type) {
                return new ZYCommentItemVH(ZYPlayFragment.this);
            }
        };

        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @OnClick({R.id.textComment, R.id.textCollect, R.id.textShare, R.id.textDown})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textDown:
                break;
            case R.id.textShare:
                break;
            case R.id.textCollect:
                break;
            case R.id.textComment:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onPlayPressed() {

    }

    @Override
    public void onSharePressed() {

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
}
