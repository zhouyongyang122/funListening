package com.funlisten.base.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.funlisten.base.viewHolder.ZYBaseViewHolder;

import java.util.List;

/**
 * Created by ZY on 17/4/8.
 */

public abstract class ZYCommonAdapter<T> extends BaseAdapter {

    private List<T> mDatas;

    public ZYCommonAdapter(){

    }

    public ZYCommonAdapter(List<T> datas) {
        this.mDatas = datas;
    }

    public void setDatas(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDatas == null){
            return 0;
        }else {
            return mDatas.size();
        }
    }

    @Override
    public T getItem(int position) {
        if (position >= 0 && position < mDatas.size()) {
            return mDatas.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ZYBaseViewHolder holder;

        if (convertView == null){
            holder = createViewHolder(getItemViewType(position));
            convertView = holder.getDataBindingRoot(parent.getContext(), parent);
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(holder.getLayoutResId(), parent, false);
            }
            holder.bindView(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ZYBaseViewHolder) convertView.getTag();
        }

        holder.updateView(mDatas.get(position), position);

        return convertView;
    }

    public abstract ZYBaseViewHolder<T> createViewHolder(int type);
}