package com.funlisten.business.album.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.funlisten.R;
import com.funlisten.base.adapter.ZYFragmentAdapter;
import com.funlisten.base.event.ZYEventLoginSuc;
import com.funlisten.base.event.ZYEventPaySuc;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.base.view.ZYLoadingView;
import com.funlisten.base.view.ZYTopTabBar;
import com.funlisten.business.album.contract.ZYAlbumHomeContract;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.presenter.ZYAlbumAudiosPresenter;
import com.funlisten.business.album.presenter.ZYAlbumDetailPresenter;
import com.funlisten.business.album.view.viewHolder.ZYAlbumFooterVH;
import com.funlisten.business.album.view.viewHolder.ZYAlbumHomeHeaderVH;
import com.funlisten.business.pay.activity.ZYPayActivity;
import com.funlisten.utils.ZYScreenUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYAlbumHomeFragment extends ZYBaseFragment<ZYAlbumHomeContract.IPresenter> implements ZYAlbumHomeContract.IView, ZYAlbumHomeHeaderVH.HeaderListener,
        ZYAlbumFooterVH.AlbumFooterListener {

    @Bind(R.id.layoutRoot)
    RelativeLayout layoutRoot;

    @Bind(R.id.layoutTop)
    LinearLayout layoutTop;

    @Bind(R.id.topBar)
    ZYTopTabBar topBar;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.coor_layout)
    CoordinatorLayout coorLayout;

    ZYFragmentAdapter adapter;

    ZYAlbumHomeHeaderVH homeHeaderVH;

    ZYLoadingView loadingView;

    ZYAlbumFooterVH footerVH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zy_fragment_album_home, container, false);

        ButterKnife.bind(this, view);

        initPager();

        initTopBar();

        initHeaderView();

        initFooterView();

        initLoadingView();

        mPresenter.load();

        return view;
    }

    private void initFooterView() {
        footerVH = new ZYAlbumFooterVH(this);
        footerVH.attachTo(layoutRoot);
        footerVH.hide();
    }

    private void initLoadingView() {
        loadingView = new ZYLoadingView(mActivity);
        loadingView.attach(layoutRoot);
        loadingView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.subscribe();
            }
        });
    }

    private void setMargin(int height) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) coorLayout.getLayoutParams();
        layoutParams.bottomMargin = ZYScreenUtils.dp2px(mActivity, height);
        coorLayout.setLayoutParams(layoutParams);
    }

    private void initHeaderView() {
        homeHeaderVH = new ZYAlbumHomeHeaderVH(this);
        homeHeaderVH.attachTo(layoutTop);
    }

    private void initTopBar() {
        ArrayList<String> tabs = new ArrayList<String>();
        tabs.add("详情.评论");
        tabs.add("全部节目");
        topBar.addTabItems(tabs, 80);
        topBar.setOnTopTabBarChangeListener(new ZYTopTabBar.OnTopTabBarChangeListener() {
            @Override
            public void onChange(int position) {
                viewPager.setCurrentItem(position);
            }
        });
    }

    private void initPager() {
        adapter = new ZYFragmentAdapter(getFragmentManager());

        ZYAlbumDetailFragment detailFragment = new ZYAlbumDetailFragment();
        new ZYAlbumDetailPresenter(detailFragment, mPresenter.getAlbumId());
        adapter.addFragment(detailFragment, "详情.评论");

        ZYAlbumAudiosFragment audiosFragment = new ZYAlbumAudiosFragment();
        new ZYAlbumAudiosPresenter(audiosFragment, new ZYAlbumModel(), mPresenter.getAlbumId());
        adapter.addFragment(audiosFragment, "全部节目");

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                topBar.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                topBar.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void showDetail(ZYAlbumDetail albumDetail) {
        homeHeaderVH.updateView(albumDetail, 0);
        if ("free".equals(albumDetail.costType)) {
            footerVH.hide();
        } else {
            mPresenter.isOrder(albumDetail.id + "");
        }
        ((ZYAlbumDetailFragment) adapter.getItem(0)).loadComments(albumDetail.details);
        ((ZYAlbumAudiosFragment) adapter.getItem(1)).setAlbumDetail(albumDetail);
    }

    @Override
    public void refreshFavorite(ZYAlbumDetail albumDetail) {
        homeHeaderVH.updateSubscribeState();
    }

    @Override
    public void refreshFollow(ZYAlbumDetail albumDetail) {
        homeHeaderVH.updateFollowState();
    }

    @Override
    public void onSubscribeClick(ZYAlbumDetail mData) {
        if (mData.isFavorite) {
            mPresenter.favoriteCancel();
        } else {
            mPresenter.favorite();
        }
    }

    @Override
    public void onFollowClick(ZYAlbumDetail mData) {
        if (mData.followSate == ZYBaseModel.FOLLOW_HAS_STATE || mData.followSate == ZYBaseModel.FOLLOW_MUTUALLY_STATE) {
            mPresenter.followCancle();
        } else {
            mPresenter.follow();
        }
    }

    @Override
    public void showLoading() {
        loadingView.showLoading();
    }

    @Override
    public void hideLoading() {
        loadingView.showNothing();
    }

    @Override
    public void showError() {
        loadingView.showError();
    }

    @Override
    public void onPay() {
        mActivity.startActivity(ZYPayActivity.createIntent(mActivity, mPresenter.getAlbumDetail()));
    }

    @Override
    public void isShowPay(boolean isShow) {
        if (isShow) {
            footerVH.show();
            setMargin(50);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ZYEventPaySuc paySuc) {
        mPresenter.subscribe();
    }
}
