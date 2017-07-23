package com.funlisten.business.photo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.photo.ZYPhoto;
import com.funlisten.business.photo.contract.ZYPhotoContract;
import com.funlisten.business.photo.contract.ZYPhotoSelect;
import com.funlisten.business.photo.view.viewholder.ZYPhotoItemVH;
import com.funlisten.utils.ZYResourceUtils;
import com.funlisten.utils.ZYScreenUtils;

import java.util.ArrayList;

/**
 * Created by gd on 2017/7/14.
 */

public class ZYPhotoFragment extends ZYListDateFragment<ZYPhotoContract.IPresenter,ZYPhoto> implements ZYPhotoContract.IView {

    private ArrayList<ZYPhoto> photeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ZYPhotoItemVH.photoSelect = photoSelect;
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRefreshRecyclerView.setRefreshEnable(false);
        mRefreshRecyclerView.getRecyclerView().setBackgroundColor(ZYResourceUtils.getColor(R.color.c8));
        mRefreshRecyclerView.getRecyclerView().setPadding(ZYScreenUtils.dp2px(mActivity,2),0,ZYScreenUtils.dp2px(mActivity,2),0);
        return view;
    }

    ZYPhotoSelect photoSelect = new ZYPhotoSelect() {
        @Override
        public void onSelect(ZYPhoto photo) {
            if(!photo.isSelect){
                photo.isSelect = true;
                photeList.add(photo);
            }else {
                photo.isSelect= false;
                photeList.remove(photo);
            }
        }
    };

    @Override
    protected void onItemClick(View view, int position) {
    }
   public void  refreshPhoto(boolean isEdit){
       ZYPhotoItemVH.isEdit = isEdit;
       mAdapter.notifyDataSetChanged();
    }

    public void clearPhoto(){
        for(ZYPhoto photo:photeList)photo.isSelect = false;
        photeList.clear();
    }
    public ArrayList<ZYPhoto> getPhoteList(){
        return photeList;
    }
    public ArrayList<ZYPhoto> getPhoto(){
        return photeList;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(mActivity, 4);
    }

    @Override
    protected ZYBaseViewHolder createViewHolder() {
        return new ZYPhotoItemVH();
    }
}
