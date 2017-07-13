package com.funlisten.business.album.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.funlisten.R;
import com.funlisten.base.adapter.ZYFragmentAdapter;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.base.view.ZYLoadingView;
import com.funlisten.base.view.ZYTopTabBar;
import com.funlisten.business.album.contract.ZYAlbumHomeContract;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.presenter.ZYAlbumAudiosPresenter;
import com.funlisten.business.album.presenter.ZYAlbumDetailPresenter;
import com.funlisten.business.album.view.viewHolder.ZYAlbumHomeHeaderVH;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYAlbumHomeFragment extends ZYBaseFragment<ZYAlbumHomeContract.IPresenter> implements ZYAlbumHomeContract.IView {

    @Bind(R.id.layoutRoot)
    RelativeLayout layoutRoot;

    @Bind(R.id.layoutTop)
    LinearLayout layoutTop;

    @Bind(R.id.topBar)
    ZYTopTabBar topBar;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    ZYFragmentAdapter adapter;

    ZYAlbumHomeHeaderVH homeHeaderVH;

    ZYLoadingView loadingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zy_fragment_album_home, container, false);

        ButterKnife.bind(this, view);

        initPager();

        initTopBar();

        initHeaderView();

        initLoadingView();

        mPresenter.load();

        return view;
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

    private void initHeaderView() {
        homeHeaderVH = new ZYAlbumHomeHeaderVH();
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
        ((ZYAlbumDetailFragment) adapter.getItem(0)).loadComments(albumDetail.details);
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
}
