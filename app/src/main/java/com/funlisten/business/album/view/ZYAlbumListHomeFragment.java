package com.funlisten.business.album.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.funlisten.R;
import com.funlisten.base.adapter.ZYFragmentAdapter;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.base.view.ZYLoadingView;
import com.funlisten.business.album.contract.ZYAlbumListHomeContract;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.album.model.bean.ZYCategory;
import com.funlisten.business.album.presenter.ZYAlbumListPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/6/13.
 * 专辑列表主页 首页更多进入
 */

public class ZYAlbumListHomeFragment extends ZYBaseFragment<ZYAlbumListHomeContract.IPresenter> implements ZYAlbumListHomeContract.IView {

    @Bind(R.id.rootLayout)
    LinearLayout rootLayout;

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    ZYLoadingView loadingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zy_fragment_ablum_list_home, container, false);
        ButterKnife.bind(this, view);
        initLoadingView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadData();
    }

    @Override
    public void refreshCategorys(List<ZYCategory> categorys) {
        ZYFragmentAdapter fragmentAdapter = new ZYFragmentAdapter(getChildFragmentManager());
        ZYAlbumListFragment ablumListFragment;
        for (ZYCategory category : categorys) {
            ablumListFragment = new ZYAlbumListFragment();
            new ZYAlbumListPresenter(ablumListFragment, new ZYAlbumModel(), category.id);
            fragmentAdapter.addFragment(ablumListFragment, category.name);
        }
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(4);
        if (categorys.size() <= 1) {
            tabLayout.setVisibility(View.GONE);
        } else {
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void initLoadingView() {
        loadingView = new ZYLoadingView(mActivity);
        loadingView.attach(rootLayout);
        loadingView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingView.showLoading();
                mPresenter.loadData();
            }
        });
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
}
