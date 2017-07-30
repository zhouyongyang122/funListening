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
import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.base.player.FZIPlayer;
import com.funlisten.base.view.ZYLoadingView;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.album.view.viewHolder.ZYCommentItemVH;
import com.funlisten.business.comment.activity.ZYCommentActivity;
import com.funlisten.business.play.activity.ZYPlayActivity;
import com.funlisten.business.play.contract.ZYPlayContract;
import com.funlisten.business.play.model.FZAudionPlayEvent;
import com.funlisten.business.play.model.ZYPLayManager;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.play.model.bean.ZYPlay;
import com.funlisten.business.play.view.viewHolder.ZYPlayActionBarVH;
import com.funlisten.business.play.view.viewHolder.ZYPlayAudiosVH;
import com.funlisten.business.play.view.viewHolder.ZYPlayHeaderVH;
import com.funlisten.utils.ZYToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.funlisten.business.play.model.ZYPLayManager.*;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayFragment extends ZYBaseFragment<ZYPlayContract.IPresenter> implements ZYPlayHeaderVH.PlayHeaderListener, ZYPlayContract.IView, ZYPlayActionBarVH.PlayActionListener, ZYCommentItemVH.CommentItemListener {

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

        headerVH = new ZYPlayHeaderVH(this);
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
                mActivity.startActivity(ZYCommentActivity.createIntent(mActivity,"audio",mPresenter.getCurPlayAudio().id+""));
                break;
        }
    }

    @Override
    public void refreshView() {
        adapter.notifyDataSetChanged();
        ZYPlay play = new ZYPlay(mPresenter.getAlbumDetail(), mPresenter.getCurPlayAudio());
        headerVH.updateView(play, 0);
        ZYPLayManager.getInstance().play(mPresenter.getCurPlayAudio(), mPresenter.getAudios());
    }

    @Override
    public void onBackPressed() {
        finish();
        mActivity.overridePendingTransition(0, R.anim.slide_down);
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

    @Override
    public void onPreClick(ZYPlay play) {
        int position = mPresenter.getAudios().indexOf(mPresenter.getCurPlayAudio());
        if (position >= 1) {
            ZYPLayManager.getInstance().play(mPresenter.getAudios().get(--position), mPresenter.getAudios());
        } else {
            ZYToast.show(mActivity, "当前为第一集");
        }
    }

    @Override
    public void onNextClick(ZYPlay play) {
        int position = mPresenter.getAudios().indexOf(mPresenter.getCurPlayAudio());
        if (position < mPresenter.getAudios().size() - 1) {
            ZYPLayManager.getInstance().play(mPresenter.getAudios().get(++position), mPresenter.getAudios());
        } else {
            ZYToast.show(mActivity, "已经是最后一集了");
        }
    }

    @Override
    public void onPlayOrPauseClick(ZYPlay play) {
        ZYPLayManager.getInstance().startOrPuase();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FZAudionPlayEvent playEvent) {
        if (playEvent != null) {
            if (playEvent.state == STATE_ERROR) {
                ZYToast.show(mActivity, "播放出错,请重新尝试!");
            } else if (playEvent.state == STATE_PREPARING) {
                showProgress();
            } else if (playEvent.state == STATE_PREPARED) {
                hideProgress();
            } else if (playEvent.state == STATE_PLAYING) {

            } else if (playEvent.state == STATE_PAUSED) {

            } else if (playEvent.state == STATE_NEED_BUY_PAUSED) {

            } else if (playEvent.state == STATE_BUFFERING_START) {

            } else if (playEvent.state == STATE_BUFFERING_END) {

            } else if (playEvent.state == STATE_PREPARING_NEXT) {
                showProgress();
            } else if (playEvent.state == STATE_COMPLETED) {

            }

            if (playEvent.audio != null) {
                mPresenter.setCurPlayAudio(playEvent.audio);
                actionBarVH.showTitle(playEvent.audio.title);
            }
            headerVH.refreshPlayState(playEvent);
        }
    }
}
