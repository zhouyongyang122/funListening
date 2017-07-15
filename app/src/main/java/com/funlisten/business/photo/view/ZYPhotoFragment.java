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

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/14.
 */

public class ZYPhotoFragment extends ZYListDateFragment<ZYPhotoContract.IPresenter,ZYPhoto> implements ZYPhotoContract.IView {

    private ArrayList<ZYPhoto> photeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ZYPhotoItemVH.photoSelect = photoSelect;
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRefreshRecyclerView.getRecyclerView().setBackgroundColor(ZYResourceUtils.getColor(R.color.c8));
        return view;
    }

    ZYPhotoSelect photoSelect = new ZYPhotoSelect() {
        @Override
        public void onSelect(ZYPhoto photo) {
            if(photo.isSelect){
                photeList.add(photo);
            }else photeList.remove(photo);
        }
    };

    @Override
    protected void onItemClick(View view, int position) {

    }

    public void clearPhoto(){
        photeList.clear();
    }

    public void upPhotoSuccess(ZYPhoto photo){

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
