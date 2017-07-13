package com.funlisten.business.main.view.viewHolder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.activity.ZYAlbumHomeActivity;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.business.album.activity.ZYAlbumListHomeActivity;
import com.funlisten.business.main.model.bean.ZYHome;

import butterknife.Bind;

/**
 * Created by ZY on 17/5/24.
 */

public class ZYHomeModulVH extends ZYBaseViewHolder<ZYHome.Module> {

    @Bind(R.id.layoutTitle)
    RelativeLayout layoutTitle;

    @Bind(R.id.imgIcon)
    ImageView imgIcon;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.layoutRefresh)
    LinearLayout layoutRefresh;
    @Bind(R.id.textRefresh)
    TextView textRefresh;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    ZYHome.Module mData;

    ZYBaseRecyclerAdapter<ZYHome.ModuleItem> mAdapter;

    @Override
    public void updateView(ZYHome.Module data, int position) {
        if (data != null) {
            mData = data;
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgIcon, data.iconImageUrl);
            textTitle.setText(data.moduleName);
            layoutTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(ZYAlbumListHomeActivity.createIntent(mContext));
                }
            });
            if (mAdapter == null) {
                mAdapter = new ZYBaseRecyclerAdapter<ZYHome.ModuleItem>() {
                    @Override
                    public ZYBaseViewHolder<ZYHome.ModuleItem> createViewHolder(int type) {
                        return new ZYHomeModulItemVH();
                    }
                };
                mAdapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ZYHome.ModuleItem moduleItem = mAdapter.getItem(position);
                        mContext.startActivity(ZYAlbumHomeActivity.createIntent(mContext, moduleItem.id));
                    }
                });
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                recyclerView.setAdapter(mAdapter);
                recyclerView.setNestedScrollingEnabled(false);
            }
            mAdapter.setDatas(data.moduleDetailDtoList);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_home_modul;
    }
}
