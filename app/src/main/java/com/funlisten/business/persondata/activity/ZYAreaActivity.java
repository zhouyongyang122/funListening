package com.funlisten.business.persondata.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.funlisten.R;
import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.base.view.ZYSwipeRefreshRecyclerView;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.persondata.view.viewholder.ZYCityVH;
import com.funlisten.business.persondata.view.viewholder.ZYProvinceVH;
import com.funlisten.business.user.model.ZYProvince;
import com.funlisten.utils.ZYScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by gd on 2017/7/23.
 */

public class ZYAreaActivity extends ZYBaseActivity {

    @Bind(R.id.city_rcy)
    ZYSwipeRefreshRecyclerView mRefreshRecyclerView;

    ZYBaseRecyclerAdapter mAdapter;

    boolean isShowCityOrPorvince = false;//false 省  true 市
    List<ZYProvince> zyProvinceArrayList = new ArrayList<>();
    List<ZYProvince.City> cityList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gd_area_layout);
        showTitle("地区");
        zyProvinceArrayList = (ArrayList<ZYProvince>) getIntent().getSerializableExtra("province");
        if(zyProvinceArrayList != null && !zyProvinceArrayList.isEmpty()){
            isShowCityOrPorvince = false;
        }
        cityList = (List<ZYProvince.City>) getIntent().getSerializableExtra("city");
        if(cityList != null && !cityList.isEmpty()){
            isShowCityOrPorvince = true;
        }
        initView();
    }
    private void initView(){
        mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRefreshRecyclerView.setLoadMoreEnable(false);
        mRefreshRecyclerView.setRefreshEnable(false);

        if(isShowCityOrPorvince){
            mAdapter = new ZYBaseRecyclerAdapter(cityList) {
                @Override
                public ZYBaseViewHolder<ZYProvince.City> createViewHolder(int type) {
                    return new ZYCityVH();
                }
            };
        }else {
            mAdapter = new ZYBaseRecyclerAdapter(zyProvinceArrayList) {
                @Override
                public ZYBaseViewHolder<ZYProvince> createViewHolder(int type) {
                    return new ZYProvinceVH();
                }
            };
        }

        mRefreshRecyclerView.getRecyclerView().setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(isShowCityOrPorvince){
                    setResult(401,new Intent().putExtra("position",position));
                }else {
                    setResult(400,new Intent().putExtra("position",position));
                }
                finish();
            }
        });

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRefreshRecyclerView.getLoadingView().getView().getLayoutParams();
        params.height = ZYScreenUtils.dp2px(mActivity, 160);
        params.topMargin = ZYScreenUtils.dp2px(mActivity, 40);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mRefreshRecyclerView.getLoadingView().getView().setLayoutParams(params);
        mRefreshRecyclerView.getLoadingView().getView().setVisibility(View.GONE);
    }


}
