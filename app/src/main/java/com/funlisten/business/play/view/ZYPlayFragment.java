package com.funlisten.business.play.view;

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
import com.funlisten.ZYApplication;
import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.event.ZYEventPlayState;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.base.player.FZIPlayer;
import com.funlisten.base.view.ZYLoadingView;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.album.view.viewHolder.ZYCommentItemVH;
import com.funlisten.business.play.contract.ZYPlayContract;
import com.funlisten.business.play.model.ZYPLayManager;
import com.funlisten.business.play.model.bean.ZYPlay;
import com.funlisten.business.play.presenter.ZYPlayPresenter;
import com.funlisten.business.play.view.viewHolder.ZYPlayActionBarVH;
import com.funlisten.business.play.view.viewHolder.ZYPlayAudiosVH;
import com.funlisten.business.play.view.viewHolder.ZYPlayHeaderVH;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayFragment extends ZYBaseFragment<ZYPlayContract.IPresenter> implements ZYPlayContract.IView, ZYPlayActionBarVH.PlayActionListener, ZYCommentItemVH.CommentItemListener {

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

    ZYLoadingView loadingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout) inflater.inflate(R.layout.zy_fragment_play, container, false);

        ButterKnife.bind(this, rootView);

        actionBarVH = new ZYPlayActionBarVH(this);
        actionBarVH.attachTo(rootView);

        audiosVH = new ZYPlayAudiosVH();

        headerVH = new ZYPlayHeaderVH();
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
        loadingView.attach(rootView);
        loadingView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.subscribe();
            }
        });
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
    public void refreshView(boolean needPlay) {
        adapter.notifyDataSetChanged();
        ZYPlay play = new ZYPlay(mPresenter.getAlbumDetail(), mPresenter.getAudio());
        headerVH.updateView(play, 0);
        ZYPLayManager.getInstance().setPlay(play);
        ZYPLayManager.getInstance().setComments(mPresenter.getComments());

        if (needPlay) {
            ZYApplication.getInstance().playService.play(play.audio);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        mActivity.overridePendingTransition(0, R.anim.slide_down);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ZYEventPlayState playState) {
        if (playState != null) {
            switch (playState.state) {
                case FZIPlayer.STATE_PREPARED:
                    break;
                case FZIPlayer.STATE_COMPLETED:
                    break;
                case FZIPlayer.STATE_ERROR:
                    break;
                case FZIPlayer.STATE_PLAYING:
                    headerVH.refreshProgress(playState.currentDuration, playState.duration);
                    break;
            }
            headerVH.refreshPlayState(playState.state);
        }
    }
}
