package com.funlisten.business.main.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.funlisten.R;
import com.funlisten.base.activity.ZYWebViewActivity;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.base.view.ZYLoadingView;
import com.funlisten.business.album.activity.ZYAlbumHomeActivity;
import com.funlisten.business.main.contract.ZYHomeContract;
import com.funlisten.business.main.model.bean.ZYHome;
import com.funlisten.business.main.view.viewHolder.ZYHomeBannerVH;
import com.funlisten.business.main.view.viewHolder.ZYHomeDayListenVH;
import com.funlisten.business.main.view.viewHolder.ZYHomeModulVH;
import com.funlisten.business.search.activity.ZYSearchActivity;
import com.funlisten.utils.ZYStatusBarUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/5/11.
 */

public class ZYHomeFragment extends ZYBaseFragment<ZYHomeContract.IPresenter> implements ZYHomeContract.IView {

    @Bind(R.id.rootView)
    RelativeLayout rootView;

    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout layout_refresh;

    @Bind(R.id.scroll_view)
    NestedScrollView scroll_view;

    @Bind(R.id.layout_module_root)
    LinearLayout layout_module_root;

    @Bind(R.id.viewTop)
    View viewTop;

    ZYLoadingView loadingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zy_fragment_home, container, false);
        ButterKnife.bind(this, view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewTop.getLayoutParams();
            layoutParams.height = ZYStatusBarUtils.getStatusBarHeight(mActivity);
            viewTop.setLayoutParams(layoutParams);
        }
        initLoadingView();
        layout_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadData();
            }
        });
        return view;
    }

    private void initLoadingView() {
        loadingView = new ZYLoadingView(mActivity);
        loadingView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.subscribe();
            }
        });
        loadingView.attach(rootView);
    }

    @OnClick({R.id.layoutSearch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutSearch:
                mActivity.startActivity(new Intent(mActivity, ZYSearchActivity.class));
                break;
        }
    }

    @Override
    public void refreshView(ZYHome home) {
        layout_refresh.setRefreshing(false);
        layout_module_root.removeAllViews();
        showBanner(home.recommendBannerDtoList);
        showDayListen(home.everyDayListeningDto);
        showModules(home.moduleDtoList);
    }

    private void showModules(List<ZYHome.Module> moduleDtoList) {
        if (moduleDtoList != null) {
            for (ZYHome.Module module : moduleDtoList) {
                ZYHomeModulVH modulVH = new ZYHomeModulVH();
                modulVH.bindView(LayoutInflater.from(mActivity).inflate(modulVH.getLayoutResId(), layout_module_root, false));
                layout_module_root.addView(modulVH.getItemView());
                modulVH.updateView(module, 0);
            }
        }
    }

    private void showDayListen(ZYHome.DayListening dayListening) {
        ZYHomeDayListenVH dayListenVH = new ZYHomeDayListenVH();
        dayListenVH.bindView(LayoutInflater.from(mActivity).inflate(dayListenVH.getLayoutResId(), layout_module_root, false));
        layout_module_root.addView(dayListenVH.getItemView());
        dayListenVH.updateView(dayListening, 0);
    }

    private void showBanner(List<ZYHome.Banner> banners) {
        if (banners != null && banners.size() > 0) {
            ZYHomeBannerVH bannerVH = new ZYHomeBannerVH(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            layout_refresh.setEnabled(false);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            layout_refresh.setEnabled(true);
                            break;
                    }
                    return false;
                }
            }, new ZYHomeBannerVH.OnHomeBannerListener() {
                @Override
                public void onBanner(ZYHome.Banner banner) {
                    if (banner.type.equals("album")) {
                        mActivity.startActivity(ZYAlbumHomeActivity.createIntent(mActivity,Integer.parseInt(banner.objectId)));
                    } else if (banner.type.equals("audio")) {

                    } else if (banner.type.equals("h5")) {
                        mActivity.startActivity(ZYWebViewActivity.createIntent(mActivity, banner.url, ""));
                    }
                }
            });
            bannerVH.bindView(LayoutInflater.from(mActivity).inflate(bannerVH.getLayoutResId(), layout_module_root, false));
            layout_module_root.addView(bannerVH.getItemView(), 0);
            bannerVH.updateView(banners, 0);
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
}
